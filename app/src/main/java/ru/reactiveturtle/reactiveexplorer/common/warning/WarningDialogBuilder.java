package ru.reactiveturtle.reactiveexplorer.common.warning;

import android.os.Bundle;

public class WarningDialogBuilder {
    private String id = "default";
    private String message = "";
    private String checkBoxText = "";
    private boolean checkBoxIsVisible = true;

    private String left = "Left", middle = "Middle", right = "Right";
    private boolean leftIsVisible = true, middleIsVisible = true, rightIsVisible = true;

    public WarningDialog build() {
        WarningDialog dialog = new WarningDialog();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("message", message);
        bundle.putString("checkBoxText", checkBoxText);
        bundle.putBoolean("checkBoxIsVisible", checkBoxIsVisible);

        bundle.putString("left", left);
        bundle.putString("middle", middle);
        bundle.putString("right", right);

        bundle.putBoolean("leftIsVisible", leftIsVisible);
        bundle.putBoolean("middleIsVisible", middleIsVisible);
        bundle.putBoolean("rightIsVisible", rightIsVisible);

        dialog.setArguments(bundle);
        return dialog;
    }

    public WarningDialogBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public WarningDialogBuilder setWarningMessage(String message) {
        this.message = message;
        return this;
    }

    public WarningDialogBuilder setCheckBoxText(String text) {
        checkBoxText = text;
        return this;
    }

    public WarningDialogBuilder setCheckBoxVisible(boolean isVisible) {
        checkBoxIsVisible = isVisible;
        return this;
    }

    public WarningDialogBuilder setButtonsText(String left, String middle, String right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
        return this;
    }

    public WarningDialogBuilder setButtonsVisible(boolean left, boolean middle, boolean right) {
        leftIsVisible = left;
        middleIsVisible = middle;
        rightIsVisible = right;
        return this;
    }
}