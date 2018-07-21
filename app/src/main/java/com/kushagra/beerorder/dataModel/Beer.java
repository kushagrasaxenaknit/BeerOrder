package com.kushagra.beerorder.dataModel;

/**
 * Created by Kushagra Saxena on 30/06/2018.
 */



        import android.support.annotation.NonNull;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Beer implements Comparable<Beer>{

    @SerializedName("abv")
    @Expose
    private String abv;
    @SerializedName("ibu")
    @Expose
    private String ibu;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("style")
    @Expose
    private String style;
    @SerializedName("ounces")
    @Expose
    private Double ounces;

    private boolean isFavourite;
    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Double getOunces() {
        return ounces;
    }

    public void setOunces(Double ounces) {
        this.ounces = ounces;
    }
    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }


    @Override
    public int compareTo(@NonNull Beer beer) {
        String alcoholContent = ((Beer) beer).getAbv();
        //ascending order
        return this.abv.compareTo(alcoholContent);
    }
}