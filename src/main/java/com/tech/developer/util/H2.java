package com.tech.developer.util;

import java.util.ListResourceBundle;

public class H2 extends ListResourceBundle {
	@Override
	protected Object[][] getContents() {
		Object[][] contents = {
				{ "driver", "org.h2.Driver" }, { "url", "jdbc:h2:file:" }, { "username", "sa" }, { "password", "" },
		};
		return contents;
	}	
}
