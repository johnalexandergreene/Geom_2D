package org.fleen.geom_2D;

import java.util.ArrayList;
import java.util.List;

/*
 * first polygon is outer edge
 * the rest are holes
 */
public class DYard extends ArrayList<DPolygon>{

  private static final long serialVersionUID=-1952265148800512304L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public DYard(List<DPolygon> polygons){
    super();
    addAll(polygons);}
  
  public DYard(int size){
    super(size);}

}
