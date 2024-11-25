package com.Project.Utils;

import javafx.fxml.FXMLLoader;

public class FxmlUtils {

    public FXMLLoader getFxmlLoader(String location){
        return new FXMLLoader(getClass().getResource(location));
    }

    private static final class InstanceHolder {
        private static final FxmlUtils instance = new FxmlUtils();
    }

    public static FxmlUtils getInstance() {
        return FxmlUtils.InstanceHolder.instance;
    }

}
