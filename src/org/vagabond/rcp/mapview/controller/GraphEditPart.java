package org.vagabond.rcp.mapview.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.vagabond.rcp.mapview.model.AttributeGraphNode;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.model.Correspondence;
import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.mapview.view.routing.RouterContainer;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.selection.VagaSelectionListener;
import org.vagabond.rcp.util.MathHelper;
import org.vagabond.rcp.util.PluginLogProvider;


public class GraphEditPart extends AbstractGraphicalEditPart 
		implements VagaSelectionListener {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(GraphEditPart.class);
	
	private static final int BORDER_GAP = 50;
	private static final int MAP_GAP = 70;
	private static final int YBORDER_GAP = 20;
	private static final int YMIN_GAP = 30;
	
	private static final Set<ModelType> interest;
	
	static {
		interest = new HashSet<ModelType> ();
		interest.add(ModelType.None);
		interest.add(ModelType.SourceRelation);
		interest.add(ModelType.TargetRelation);
		interest.add(ModelType.Mapping);
		interest.add(ModelType.Correspondence);
	}
	
	public GraphEditPart(Graph graph) {
		setModel(graph);
	}
	
	@Override
	public void activate() {
		super.activate();
		GlobalSelectionController.addSelectionListener(this);
	}
	
	@Override
	public void deactivate() {
		unregisterRouters();
		GlobalSelectionController.removeSelectionListener(this);
		super.deactivate();
	}
		
	
	@Override
	/**
	* Create root figure. Use standard FreeformLayer figure here.
	*/
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		layer.setOpaque(true);
		layer.setLayoutManager(new FreeformLayout()); 
		layer.setBorder(new LineBorder(1));
		
		registerRouters(layer);
		
		return layer;
	}
	
	private void registerRouters (IFigure pane) {
		ShortestPathConnectionRouter sh;
		ManhattanConnectionRouter man;
		
		sh= new ShortestPathConnectionRouter(pane);
		sh.setSpacing(10);

		RouterContainer.getInstance().registerRouter("Correspondence", sh);
		
		man = new ManhattanConnectionRouter();
				
		RouterContainer.getInstance().registerRouter("ForeignKey", man);
	}
	
	private void unregisterRouters () {
		RouterContainer.getInstance().dropRouters();
	}

	//Return the lower elements of the model
	protected List<?> getModelChildren() {
		return ((Graph)getModel()).getChildren();
	}
	
	@Override
	protected void createEditPolicies() {
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
	}
	
	public void setLayoutConstraints () {
		Graph graphModel = (Graph) this.getModel();
		Vector<RelationNodeEditPart> sourceRelEdits;
		Vector<RelationNodeEditPart> targetRelEdits;
		Vector<MappingNodeEditPart> mappingEdits;
		int curX = 0;
		int sourceWidth, targetWidth, mapWidth, viewWidth, totalWidth;
		int sourceHeight, targetHeight, mapHeight, viewHeight, // maxHeight, 
			totalHeight;
		int sourceY, sourceGapY, targetY, targetGapY, mapY, mapGapY;
		
		if (children == null)
			return;
		
		// create lists for each type of children (source, target, map)
		sourceRelEdits = new Vector<RelationNodeEditPart>();
		targetRelEdits = new Vector<RelationNodeEditPart>();
		mappingEdits = new Vector<MappingNodeEditPart>();
	
		for(Object child: children) {
			if (child instanceof RelationNodeEditPart) {
				RelationNodeEditPart childRel = (RelationNodeEditPart) child;
				RelationGraphNode modelElem = (RelationGraphNode) childRel.getModel();
				if (graphModel.isSourceRelation(modelElem))
					sourceRelEdits.add(childRel);
				else
					targetRelEdits.add(childRel);
			} else if (child instanceof MappingNodeEditPart) {
				mappingEdits.add((MappingNodeEditPart)child);
			}
		}
		
		// get maximum width for each of the three columns (source, target, map)
		// if gaps + all maximum widths is bigger than the views width then
		// make sure all three columns are wide enough. Otherwise grow these columns
		// to fill the width of the view 
		sourceWidth = getMaxWidth(sourceRelEdits);
		targetWidth = getMaxWidth(targetRelEdits);
		mapWidth = getMaxWidth(mappingEdits);
		viewWidth = this.getViewer().getControl().getBounds().width;
		totalWidth = sourceWidth + targetWidth + mapWidth + (2 * BORDER_GAP) 
				+ (2 * MAP_GAP);
		
		sourceHeight = getMaxHeight(sourceRelEdits);
		targetHeight = getMaxHeight(targetRelEdits);
		mapHeight = getMaxHeight(mappingEdits);
		viewHeight = this.getViewer().getControl().getBounds().height;
//		maxHeight = MathHelper.max(sourceHeight,targetHeight, mapHeight); 
		totalHeight = MathHelper.max(addGaps(sourceHeight, sourceRelEdits.size()),
				addGaps(targetHeight, targetRelEdits.size()),
				addGaps(mapHeight, mappingEdits.size()),
				viewHeight);
		
		log.debug("max widths source: " + sourceWidth 
				+ " target: " + targetWidth 
				+ " maps: " + mapWidth
				+ " = total : " + totalWidth 
				+ " viewer: " + viewWidth);
		log.debug("max heights source: " + sourceHeight 
				+ " target: " + targetHeight 
				+ " maps: " + mapHeight
				+ " = total : " + totalHeight
				+ " viewer: " + viewHeight);
		
		sourceY = getY (sourceRelEdits, sourceHeight, totalHeight);
		sourceGapY = getYGap (sourceRelEdits, sourceHeight, totalHeight);;
		mapY = getY (mappingEdits, mapHeight, totalHeight);
		mapGapY = getYGap (mappingEdits, mapHeight, totalHeight);;
		targetY = getY (targetRelEdits, targetHeight, totalHeight);
		targetGapY = getYGap (targetRelEdits, targetHeight, totalHeight);;
		
		if (totalWidth > viewWidth) {
			curX = layoutSourceRelations (sourceRelEdits, sourceY, sourceGapY);
			curX = layoutMappings(curX, mappingEdits, mapY, mapGapY);
			curX = layoutTargetRelations (curX, targetRelEdits, targetY, targetGapY);
		}
		else {
			layoutSourceRelations (sourceRelEdits, sourceY, sourceGapY);
			
			curX = (viewWidth + (MAP_GAP * 2) - totalWidth) / 2;
			curX = BORDER_GAP + sourceWidth + curX - MAP_GAP;
			layoutMappings(curX, mappingEdits, mapY, mapGapY);
			
			curX = viewWidth - (BORDER_GAP + MAP_GAP)- targetWidth; 
			layoutTargetRelations(curX, targetRelEdits, targetY, targetGapY);
		}
	}
	
	private int getY (Vector<? extends Object> ob, int height, int totalHeight) {
		int numElem = ob.size();
		int result;
		
		if (numElem == 1)
			result = (totalHeight - height) / 2;
		else
			result = YBORDER_GAP;
		log.debug("have startY " + result);
		return result;
	}
	
	private int getYGap (Vector<? extends Object> ob, int height, int totalHeight) {
		int numElem = ob.size();
		int result;
		
		if (numElem == 1)
			result = (totalHeight - height) / 2;
		else
			result = (totalHeight - height - (2 * YBORDER_GAP)) / 
					(numElem - 1);
		log.debug("have gapY " + result);
		return result;
	}
	
	private int addGaps (int height, int numParts) {
		return height + (2 * YBORDER_GAP) + ((numParts - 1) * YMIN_GAP); 
	}
	
	private int getMaxWidth (Vector<? extends AbstractGraphicalEditPart> parts) {
		int maxWidth = 0;
		for(AbstractGraphicalEditPart part: parts)
			maxWidth = Math.max(maxWidth, part.getFigure().getBounds().width);
		
		return maxWidth;
	}
	
	private int getMaxHeight (Vector<? extends AbstractGraphicalEditPart> parts) {
		int maxHeight = 0;
		for(AbstractGraphicalEditPart part: parts)
			maxHeight += part.getFigure().getBounds().height;
		
		return maxHeight;
	}

	
	
	private int layoutSourceRelations (Vector<RelationNodeEditPart> sourceRels, 
			int startY, int gapY) {
		int maxWidth = 0, xPos, yPos;
		
		xPos = BORDER_GAP; yPos = startY; 
		
		log.debug("layout source starting (X,Y) + (" + xPos + "," + yPos + 
				") with yGap " + gapY);
		
		for(RelationNodeEditPart relEdit: sourceRels) {
			RelationGraphNode node = (RelationGraphNode) relEdit.getModel();
			Rectangle nodeBounds = relEdit.getFigure().getBounds();
			Rectangle newBounds = new Rectangle(xPos, yPos, -1, -1);
			maxWidth = Math.max(maxWidth, nodeBounds.width);
			
			this.setLayoutConstraint(relEdit, relEdit.getFigure(), newBounds);
			log.debug ("replace bounds " + nodeBounds.toString() + " for " 
					+ node.getName() + " are " + newBounds.toString());
			yPos = yPos + gapY + nodeBounds.height;
		}
		
		return xPos + maxWidth;
	}
	
	private int layoutTargetRelations (int curX, Vector<RelationNodeEditPart> targetRels,
			int startY, int gapY) {
		int maxWidth, xPos, yPos;
		
		xPos = curX + MAP_GAP;
		yPos = startY;
		maxWidth = 0; 

		log.debug("layout targets starting (X,Y) + (" + xPos + "," + yPos + 
				") with yGap " + gapY);

		
		for(RelationNodeEditPart relEdit: targetRels) {
			RelationGraphNode node = (RelationGraphNode) relEdit.getModel();
			Rectangle bounds = relEdit.getFigure().getBounds();
			Rectangle newBounds = new Rectangle(xPos, yPos, -1, -1);
			
			this.setLayoutConstraint(relEdit, relEdit.getFigure(), 
					newBounds);
			yPos = yPos + gapY + bounds.height;
			
			log.debug ("replace bounds " + bounds.toString() + " for " 
					+ node.getName() + " are " + newBounds.toString());
		}
		
		return xPos + maxWidth;
	}
	
	private int layoutMappings (int curX, Vector<MappingNodeEditPart> mappings, 
			int yStart, int yGap) {
		int maxWidth, xPos, yPos;
		
		xPos = curX + MAP_GAP ; 
		maxWidth = 0; 
		yPos = yStart;
		
		log.debug("layout mappings starting (X,Y) + (" + xPos + "," + yPos + 
				") with yGap " + yGap);
		
		for(MappingNodeEditPart mapEdit: mappings) {
			MappingGraphNode node = (MappingGraphNode) mapEdit.getModel();
			Rectangle bounds = mapEdit.getFigure().getBounds();
			Rectangle newBounds = new Rectangle(xPos, yPos, -1, -1); 
			this.setLayoutConstraint(mapEdit, mapEdit.getFigure(), newBounds);
			
			log.debug ("replace bounds " + bounds.toString() + " for " 
					+ node.getName() + " are " + newBounds.toString());
			maxWidth = Math.max(maxWidth, bounds.width);
			yPos += yGap + bounds.height;
		}
		
		return xPos + maxWidth;
	}

	@Override
	public void event(VagaSelectionEvent e) {
		Graph graph = (Graph) getModel();
		
		if (e.isEmpty()) {
			deselectEverything();
		} else if (e.isReset()) {
			deselectEverything();
		}
		
		if (e.isLimitScope()) {
			//TODO what to do
		}
		else {
			switch(e.getElementType()) {
			case Mapping:
				for(String id: e.getElementIds()) {
					MappingGraphNode map = graph.getMapping(id);
					MappingNodeEditPart mapPart = (MappingNodeEditPart) 
							getViewer().getEditPartRegistry().get(map);
					mapPart.nonUserChangeSelection(true);
				}
				break;
			case Correspondence:
				for(String id: e.getElementIds()) {
					Correspondence corr = graph.getCorrespondence(id.toUpperCase());
					CorrespondenceEditPart corrPart = (CorrespondenceEditPart) 
							getViewer().getEditPartRegistry().get(corr);
					corrPart.nonUserChangeSelection(true);
				}
				break;
			case SourceRelation:
				for(String id: e.getElementIds()) {
					RelationGraphNode rel = graph.getSourceSchema()
							.getUnqualRel(id);
					RelationNodeEditPart relPart = (RelationNodeEditPart) 
							getViewer().getEditPartRegistry().get(rel);
					relPart.nonUserChangeSelection(true);
				}
				break;
			case TargetRelation:
				for(String id: e.getElementIds()) {
					RelationGraphNode rel = graph.getTargetSchema()
							.getUnqualRel(id);
					RelationNodeEditPart relPart = (RelationNodeEditPart) 
							getViewer().getEditPartRegistry().get(rel);
					relPart.nonUserChangeSelection(true);
				}
				break;
			}
		}
	}

	private void deselectEverything() {
		for(Object child: getChildren()) {
			if (child instanceof MappingNodeEditPart) {
				((MappingNodeEditPart) child).nonUserChangeSelection(false);
			}
			if (child instanceof RelationNodeEditPart) {
				RelationNodeEditPart relPart = ((RelationNodeEditPart) child); 
				RelationGraphNode rel = (RelationGraphNode) relPart.getModel();
				relPart.nonUserChangeSelection(false);
				
				// find corr edit parts
				if (rel.isSourceRel()) {
					for(AttributeGraphNode attr: rel.getAttributes()) {
						for(Connection con: attr.getSourceConnections()) {
							if (con instanceof Correspondence) {
								CorrespondenceEditPart conPart;
								conPart = (CorrespondenceEditPart) getViewer()
										.getEditPartRegistry().get(con);
								conPart.nonUserChangeSelection(false);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Set<ModelType> interestedIn() {
		return interest;
	}


	
}