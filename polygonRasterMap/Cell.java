package org.fleen.geom_2D.polygonRasterMap;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DPolygon;

/*
 * A cell has a location in the array (x,y) and a number of polygon presences
 * A polygon presence is the presence of a polygon. 
 *   A reference to a polygon and a value describing how present that polygon is range : [0,1]
 * Presences come in 2 varieties
 * Interior presence means that the cell is inside the polygon, presence is 1.0
 * Edge presence means that the cell is near the edge of the polygon.
 *   Right at the edge presence is 0.5. 
 *   Move inward and the presence increases to 1.0
 *   Move outward and the presence decreases to 0.0 
 * 
 */
class Cell{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  /*
   * xcoor, ycoor, center x, center y, the cellarray that contains this cell
   */
  Cell(PolygonRasterMap rastermap,int x,int y){
    this.rastermap=rastermap;
    this.x=x;
    this.y=y;}
  
  Cell(PolygonRasterMap rastermap,int x,int y,boolean offmap){
    this(rastermap,x,y);
    this.offmap=offmap;}
  
  /*
   * ################################
   * RASTER MAP
   * ################################
   */
  
  PolygonRasterMap rastermap;
  //the cell may be off the raster map. it happens when we're 
  //rendering a polygon that's only partially within the viewport
  boolean offmap=false;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  //coors within the cellarray
  //the cell center is also the cell coors
  int x,y;
  
  List<Cell> getNeighbors(){
    List<Cell> n=new ArrayList<Cell>(8);
    //N
    Cell a=rastermap.getCell(x,y+1);
    if(a!=null)n.add(a);
    //NE
    a=rastermap.getCell(x+1,y+1);
    if(a!=null)n.add(a);
    //E
    a=rastermap.getCell(x+1,y);
    if(a!=null)n.add(a);
    //SE
    a=rastermap.getCell(x+1,y-1);
    if(a!=null)n.add(a);
    //S
    a=rastermap.getCell(x,y-1);
    if(a!=null)n.add(a);
    //SW
    a=rastermap.getCell(x-1,y-1);
    if(a!=null)n.add(a);
    //W
    a=rastermap.getCell(x-1,y);
    if(a!=null)n.add(a);
    //NW
    a=rastermap.getCell(x-1,y+1);
    if(a!=null)n.add(a);
    //
    return n;}
  
  /*
   * this is for getting neighbor cells in a polygoncellmap creation process
   * we sometimes want to refer to cells that are in the polygoncellmap.cellcache but not
   * in the rastermap. So we check the cache first.
   */
  List<Cell> getNeighbors(PolygonCells m){
    List<Cell> n=new ArrayList<Cell>(8);
    //N
    Cell a=m.getCell(x,y+1);
    if(a!=null)n.add(a);
    //NE
    a=m.getCell(x+1,y+1);
    if(a!=null)n.add(a);
    //E
    a=m.getCell(x+1,y);
    if(a!=null)n.add(a);
    //SE
    a=m.getCell(x+1,y-1);
    if(a!=null)n.add(a);
    //S
    a=m.getCell(x,y-1);
    if(a!=null)n.add(a);
    //SW
    a=m.getCell(x-1,y-1);
    if(a!=null)n.add(a);
    //W
    a=m.getCell(x-1,y);
    if(a!=null)n.add(a);
    //NW
    a=m.getCell(x-1,y+1);
    if(a!=null)n.add(a);
    //
    return n;}
  
  /*
   * ################################
   * POLYGON PRESENCES
   * ################################
   */
  
  /*
   * TODO optimize this
   * make it just bigger than the biggest size we expect
   */
  private static final int INITPRESENCELISTSIZE=10;
  
  List<Presence> presences=new ArrayList<Presence>(INITPRESENCELISTSIZE);
  
  void addPresence(Presence p){
    presences.add(p);}
  
  void addPresence(DPolygon polygon,double intensity){
    addPresence(new Presence(polygon,intensity));}
  
  /*
   * this is for interior presence.
   * intensity is assumed to be 1.0
   */
  void addPresence(DPolygon polygon){
    addPresence(polygon,1.0);}
  
  /*
   * returns true if the specified polygon has nonzero presence at this cell
   */
  boolean hasPresence(DPolygon polygon){
    for(Presence p:presences)
      if(p.polygon==polygon)
        return true;
    return false;}
  
  /*
   * return the intensity of the specified polygon's presence at this cell
   */
  double getPresenceIntensity(DPolygon polygon){
    for(Presence p:presences)
      if(p.polygon==polygon)
        return p.intensity;
    return 0;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  private static final int PRIME=104729;
  
  public int hashCode(){
    return x+y*PRIME;}
  
  public boolean equals(Object a){
    Cell b=(Cell)a;
    return b.x==x&&b.y==y;}

}
