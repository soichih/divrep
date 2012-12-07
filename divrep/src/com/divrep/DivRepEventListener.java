package com.divrep;

import java.io.Serializable;

abstract public class DivRepEventListener implements Serializable {
	abstract public void handleEvent(DivRepEvent e);
}
