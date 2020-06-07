package com.afaqy.avl.webnotifier.model;

import java.util.List;

/**
 * Name :WebSocketRequest
 * <br>
 * Description : Is a model for mapping web request of updated get requested updated units
 * <br>
 * Date : 26/04/2020
 * <br>
 * Create by : Mona Adel
 * <br>
 * Mail : mona.adel@afaqy.com
 */
public class WebSocketRequest {
   private String action;
   private List<String> units;

   public WebSocketRequest(String action, List<String> units) {
      this.action = action;
      this.units = units;
   }

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public List<String> getUnits() {
      return units;
   }

   public void setUnits(List<String> units) {
      this.units = units;
   }

   @Override
   public String toString() {
      return "WebSocketRequest{" +
              "action='" + action + '\'' +
              ", units=" + units +
              '}';
   }
}
