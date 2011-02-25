package org.iplantc.simple.client;

import org.iplantc.phyloviewer.client.tree.viewer.DetailView;
import org.iplantc.phyloviewer.client.tree.viewer.model.JsDocument;
import org.iplantc.phyloviewer.shared.model.Document;
import org.iplantc.phyloviewer.shared.model.IDocument;
import org.iplantc.phyloviewer.shared.render.style.IStyleMap;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.JsonUtils;

public abstract class TreePanel extends ContentPanel
{
	TreePanel()
	{
		setStyleAttribute("margin", "5px");
		setHeading("Tree view");

		setTopComponent(buildToolbar());
	}

	private Button buildHomeButton()
	{
		Button button = new Button("Home", new SelectionListener<ButtonEvent>()
		{

			@Override
			public void componentSelected(ButtonEvent ce)
			{
				DetailView view = getView();
				if(view != null)
				{
					view.zoomToFit();
				}
			}
		});

		return button;
	}

	private Button buildStyleButton()
	{
		Button button = new Button("Style", new SelectionListener<ButtonEvent>()
		{

			@Override
			public void componentSelected(ButtonEvent ce)
			{
				final Dialog dialog = new Dialog();
				dialog.setButtons(Dialog.OKCANCEL);

				final TextArea textArea = new TextArea();
				textArea.setSize(800, 300);
				dialog.add(textArea);

				dialog.getButtonById("ok").addSelectionListener(new SelectionListener<ButtonEvent>()
				{
					@Override
					public void componentSelected(ButtonEvent ce)
					{
						DetailView view = getView();
						if(view != null)
						{
							IDocument document = view.getDocument();
							if(document != null)
							{
								String json = textArea.getValue();
								IStyleMap styleMap = getStyleMap(json);
								document.setStyleMap(styleMap);
								view.setDocument(document);
								view.requestRender();
							}
						}

						dialog.hide();
					}
				});

				dialog.getButtonById("cancel").addSelectionListener(new SelectionListener<ButtonEvent>()
				{
					@Override
					public void componentSelected(ButtonEvent ce)
					{
						dialog.hide();
					}
				});

				dialog.show();
			}
		});

		return button;
	}

	private ToolBar buildToolbar()
	{
		ToolBar toolbar = new ToolBar();

		toolbar.add(new Button("Open", new SelectionListener<ButtonEvent>()
		{

			@Override
			public void componentSelected(ButtonEvent ce)
			{
				loadFile();
			}
		}));

		toolbar.add(buildHomeButton());
		toolbar.add(buildStyleButton());

		return toolbar;
	}

	private void loadFile()
	{
		final Dialog dialog = new Dialog();
		dialog.setButtons(Dialog.OKCANCEL);

		final FormPanel panel = new FormPanel();
		panel.setHeading("Load file");
		panel.setFrame(true);
		panel.setAction("/parseFile");
		panel.setEncoding(Encoding.MULTIPART);
		panel.setMethod(Method.POST);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setWidth(350);

		FileUploadField file = new FileUploadField();
		file.setAllowBlank(false);
		file.setName("uploadedfile");
		file.setFieldLabel("File");
		panel.add(file);

		dialog.getButtonById("ok").addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if(!panel.isValid())
				{
					return;
				}

				panel.submit();

			}
		});

		dialog.getButtonById("cancel").addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				dialog.hide();
			}
		});

		panel.addListener(Events.Submit, new Listener<FormEvent>()
		{
			public void handleEvent(FormEvent arg0)
			{
				String result = arg0.getResultHtml();
				setJSONData(result);

				dialog.hide();
			}
		});

		dialog.add(panel);

		dialog.show();
	}

	void setJSONData(String treeData)
	{
		JsDocument doc = getDocument("(" + treeData + ") ");

		Document document = new Document();
		document.setTree(doc.getTree());
		document.setStyleMap(doc.getStyleMap());
		document.setLayout(doc.getLayout());

		DetailView view = getView();
		if(view != null)
		{
			view.setDocument(document);
			view.requestRender();
		}
	}

	private final static native JsDocument getDocument(String json) /*-{
		return eval(json);
	}-*/;

	private static IStyleMap getStyleMap(String json)
	{
		return (IStyleMap)JsonUtils.safeEval(json);
	}

	public abstract DetailView getView();
}