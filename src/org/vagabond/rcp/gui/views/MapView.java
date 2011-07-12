package org.vagabond.rcp.gui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.vagabond.rcp.gui.views.mapview.RelationGraphNode;

public class MapView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.mapview";
	private Graph graph;
	// Source Relation GraphContainers
	private Map<String,RelationGraphNode> sourceRelConts;
	// Target Relation GraphContainers
	private Map<String,RelationGraphNode> targetRelConts;
	// Correspondence Edges
	private Map<String, GraphConnection> corrEdges;
	
	public static MapView getInstance() {
		return (MapView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		graph = new Graph(parent, SWT.NONE);
		LayoutAlgorithm layout = setLayout();
		graph.setLayoutAlgorithm(layout, true);
		graph.applyLayout();
		init();
	}
	
	private void init () {
		sourceRelConts = new HashMap<String, RelationGraphNode> ();
		targetRelConts = new HashMap<String, RelationGraphNode> ();
		corrEdges = new HashMap<String, GraphConnection> ();
	}
	
	// TODO: Custom Layout
	private LayoutAlgorithm setLayout() {
		LayoutAlgorithm layout;
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		//layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		 layout = new
		 HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;
	}

	@Override
	public void setFocus() {
	}
	
	public void addRel(String name) {
		RelationGraphNode newNode = new RelationGraphNode(graph, SWT.NONE, name);
		// TODO: error checking
		if (name.startsWith("source.")) {
			sourceRelConts.put(name, newNode);
		} else if (name.startsWith("target.")) {
			targetRelConts.put(name, newNode);
		} else {
			// error
		}
		
		LayoutAlgorithm layout = setLayout();
		newNode.setLayoutAlgorithm(layout, true);
	}
	
	public void addAttr(String relName, String attrName) {
		// TODO: error checking
		RelationGraphNode r;
		GraphNode newNode;
		if (relName.startsWith("source.")) {
			if ((r = sourceRelConts.get(relName)) != null) {
				newNode = new GraphNode(r, SWT.NONE, attrName);
				r.addAttrNode(attrName, newNode);
			} else {
				// no such relation found
			}
		} else if (relName.startsWith("target.")) {
			if ((r = targetRelConts.get(relName)) != null) {
				newNode = new GraphNode(r, SWT.NONE, attrName);
				r.addAttrNode(attrName, newNode);
			} else {
				// no such relation found
			}
		} else {
			// error
		}
	}
	/*
	 * Add correspondence/GraphConnection 'corrName' between sourceAttr node and targetAttr node
	 */
	public void addCorrespondence (String corrName, String sourceRel, String sourceAttr, String targetRel, String targetAttr) {
		GraphNode sourceNode;
		GraphNode targetNode;
		GraphConnection corrEdge;
		
		// TODO: error checking
		sourceNode = sourceRelConts.get(sourceRel).getAttrNode(sourceAttr);
		targetNode = targetRelConts.get(targetRel).getAttrNode(targetAttr);
		corrEdge = new GraphConnection(graph, SWT.NONE, sourceNode, targetNode);
		corrEdge.setText(corrName);
		corrEdge.setLineWidth(3);
		corrEdges.put(corrName, corrEdge);
		
		// Hack to make correspondences display properly
		for (RelationGraphNode node : sourceRelConts.values()) {
			node.open(false);
			node.close(false);
			node.applyLayout();
		}
		for (RelationGraphNode node : targetRelConts.values()) {
			node.open(false);
			node.close(false);
			node.applyLayout();
		}
	}
}