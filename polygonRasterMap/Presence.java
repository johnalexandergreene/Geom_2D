package org.fleen.geom_2D.polygonRasterMap;

import org.fleen.geom_2D.DPolygon;

public class Presence{
  
  Presence(DPolygon polygon,double intensity){
    this.polygon=polygon;
    this.intensity=intensity;}
  
  DPolygon polygon;
  double intensity;//range 0..1

}
