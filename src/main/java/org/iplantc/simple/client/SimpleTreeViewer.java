package org.iplantc.simple.client;

import org.iplantc.core.broadcaster.shared.BroadcastCommand;

import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SimpleTreeViewer implements EntryPoint
{
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		TreePanel treeWidget = new TreePanel();
		
		final TextArea textArea = new TextArea();
		textArea.setSize(800, 300);

		treeWidget.getView().setBroadcastCommand(new BroadcastCommand()
		{

			@Override
			public void broadcast(String jsonMsg)
			{
				String value = textArea.getValue();
				if(value == null)
				{
					value = "";
				}
				
				textArea.setValue(value + jsonMsg + "\n");
			}
		});

		Viewport viewport = new Viewport();
		viewport.add(treeWidget, new MarginData(10));
		viewport.add(textArea, new MarginData(10));
		RootPanel.get().add(viewport);
	}
}
