package com.sync.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YH on 2017-02-17.
 */

public class Suggestion {

  @SerializedName("conmf") public Comfort comfort;

  @SerializedName("cw") public CarWash carWash;

  public Sport sport;

  public class Comfort {
    @SerializedName("txt") public String info;
  }

  public class CarWash {
    @SerializedName("txt") public String info;
  }

  public class Sport {
    @SerializedName("txt") public String info;
  }
}