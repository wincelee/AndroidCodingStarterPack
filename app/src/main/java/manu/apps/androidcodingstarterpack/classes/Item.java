package manu.apps.androidcodingstarterpack.classes;

import org.jetbrains.annotations.NotNull;

public class Item {

    int itemId;
    String itemName;

    public Item() {

    }

    public Item(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @NotNull
    @Override
    public String toString() {
        return itemName;
    }
}
