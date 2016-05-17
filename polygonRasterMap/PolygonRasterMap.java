package org.fleen.geom_2D.polygonRasterMap;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.fleen.geom_2D.DPolygon;

/*
 * Map 0..n DPolygons to a raster.
 * The raster is a rectangular array of 1x1 cells
 * We can do a transform
 * The polygons are mapped to cells as Presences
 * For each cell there are 0..n polygons that manifest there as a Presence.
 *   1 Presence for each Polygon there.
 *   The intensity of the Presence corrosponds roughly to the degree to which the polygon covers the cell-square.
 *   Total coverage means intensity of 1.0. Partial or near-the-edge means less than 1.0.
 *   Right on the edge is intensity=0.5  
 */
public class PolygonRasterMap implements Iterable<Cell>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  //empty
  public PolygonRasterMap(int w,int h,AffineTransform t,double glowspan){
    this.glowspan=glowspan;
    this.transform=t;
    initCells(w,h);}
  
  //1 polygon
  public PolygonRasterMap(int w,int h,AffineTransform t,double glowspan,DPolygon polygon){
    this(w,h,t,glowspan);
    castPresence(polygon);}
  
  //a list of polygons
  public PolygonRasterMap(int w,int h,AffineTransform t,double glowspan,List<DPolygon> polygons){
    this(w,h,t,glowspan);
    castPresence(polygons);}
  
  //an array of polygons
  public PolygonRasterMap(int w,int h,AffineTransform t,double glowspan,DPolygon... polygons){
    this(w,h,t,glowspan);
    castPresence(polygons);}
  
  /*
   * ################################
   * TRANSFORM
   * ################################
   */
  
  AffineTransform transform;
  
  /*
   * ################################
   * GLOWSPAN
   * ################################
   */
  
  double glowspan;
  
  /*
   * ################################
   * CELLS
   * A cell's center is its coordinates
   * its square ranges x-0.5 to x+0.5 and y-0.5 to y+0.5
   * ################################
   */
  
  /*
   * the height and width of a cell
   */
  static final double CELLSPAN=1.0;
  
  Cell[][] cells;
  
  int cellarraywidth,cellarrayheight;
  
  private void initCells(int w,int h){
    cellarraywidth=w;
    cellarrayheight=h;
    cells=new Cell[w][h];
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        cells[x][y]=new Cell(this,x,y);}}}
  
  /*
   * return the cell that contains the specified point
   * the cell's coors are also the cell's center point
   * the cell's square spans cell.x-0.5 to cell.y+0.5 and cell.y-0.5 to cell.y+0.5
   */
  Cell getCellContainingPoint(double x,double y){
    //TODO this could be improved
    if(x-Math.floor(x)<0.5)
      x=Math.floor(x);
    else
      x=Math.ceil(x);
    if(y-Math.floor(y)<0.5)
      y=Math.floor(y);
    else
      y=Math.ceil(y);
    return getCell((int)x,(int)y);}
  
  Cell getCell(int x,int y){
    //if the cell is off array then create it and return it
    if(isOffMap(x,y)){
      return new Cell(this,x,y,true);
    //if the cell is on the array then return that cell
    }else{
      return cells[x][y];}}
  
  boolean isOffMap(int x,int y){
    return x<0||x>=cellarraywidth||y<0||y>=cellarrayheight;}
  
  public Iterator<Cell> iterator(){
    return new RasterCellIterator(this);}
  
  /*
   * ################################
   * MAP POLYGON TO CELLS
   * Cast the presence of the polygon, like a shadow on the cells
   * The polygon is assumed to be in the viewport, all or part.
   * If the polygon is not in the viewport then resources have been wasted but it will not result in an error.
   * ################################
   */
  
  public PolygonCells castPresence(DPolygon polygon){
    //cast the presence, create the polygoncellmap and return it
    //if the polygon crosses the edge of the viewport then the polygoncellmap will contain some cells that are not within this rastermap
    //the polygoncellmap is just a way of doing the cell presences in an orderly way
    //Yes we return the PolygonCells object but after this method we will probably never use PolygonCells except for debugging and maybe tools 
    PolygonCells pcm=new PolygonCells(this,polygon);
    return pcm;}
  
  public void castPresence(List<DPolygon> polygons){
//    Map<DPolygon,PolygonCells> maps=new Hashtable<DPolygon,PolygonCells>();
//    PolygonCells pcm;
    for(DPolygon polygon:polygons)
      castPresence(polygon);}
  
  public void castPresence(DPolygon... polygons){
    castPresence(Arrays.asList(polygons));}
  
}
