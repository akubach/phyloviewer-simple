package org.iplantc.simple.client;

import org.iplantc.phyloviewer.client.tree.viewer.DetailView;
import org.iplantc.phyloviewer.client.tree.viewer.FixedDetailView;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ScrollListener;

public class ScrollableTreePanel extends TreePanel
{
	FixedDetailView view;

	public ScrollableTreePanel()
	{
		setScrollMode(Scroll.AUTOY);
		
		view = new FixedDetailView(800, 600);
		this.add(view);
		this.setSize("800", "600");
		
		this.addScrollListener(new ScrollListener()
		{
			@Override
			public void widgetScrolled(ComponentEvent ce)
			{
				setViewableArea();
				DetailView view = getView();
				if(view != null)
				{
					view.requestRender();
				}
			}
		});
	}

	private void setViewableArea()
	{
		if(view != null)
		{
			int x = getHScrollPosition();
			int y = getVScrollPosition();
			view.setViewableArea(x, y, getWidth(), getHeight());
		}
	}
	
	@Override
	public DetailView getView()
	{
		return view;
	}
}
