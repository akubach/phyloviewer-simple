package org.iplantc.simple.client;

import org.iplantc.phyloviewer.client.events.NavigationMode;
import org.iplantc.phyloviewer.client.tree.viewer.DetailView;

public class AutoCollapseTreePanel extends TreePanel
{
	DetailView view;
	
	public AutoCollapseTreePanel()
	{
		view = new DetailView(800,600);
		
		NavigationMode navMode = new NavigationMode(view);
		view.setInteractionMode(navMode);

		this.add(view);
		this.setSize("800", "600");
	}

	@Override
	public DetailView getView()
	{
		return view;
	}
}
