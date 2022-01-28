package com.atuvwapps.drawingsbyelle.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("Drawing")
    private Drawing drawing;
    @SerializedName("Size")
    private String size;
    @SerializedName("Quantity")
    private int quantity;

    public OrderItem(){}

    public OrderItem(Drawing drawing, String size, int quantity){
        this.drawing = drawing;
        this.size = size;
        this.quantity = quantity;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
