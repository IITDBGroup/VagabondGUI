package org.vagabond.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.vagabond.rcp.gui.views.ExplRankView;
import org.vagabond.rcp.model.ContentProvider;

public class SwitchRankingHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ContentProvider.getInstance().switchRanking();
		if (ContentProvider.getInstance().getExplCol() != null) { 
			ContentProvider.getInstance().getExplCol().resetIter();
    		if (ContentProvider.getInstance().getExplCol().hasNext())
    			ExplRankView.getInstance().updateView(ContentProvider
    					.getInstance().getExplCol().next());
		}
		return null;
	}

}
