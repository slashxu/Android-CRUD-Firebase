package com.soliwork.gamestation.Model;

public class Produtos {

    private String uid;
    private String nameProduto;
    private String nameCategoria;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNameProduto() {
        return nameProduto;
    }

    public void setNameProduto(String nameProduto) {
        this.nameProduto = nameProduto;
    }

    public String getNameCategoria() {
        return nameCategoria;
    }

    public void setNameCategoria(String nameCategoria) {
        this.nameCategoria = nameCategoria;
    }

    @Override
    public String toString() {
        return nameProduto;
    }
}
