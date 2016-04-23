package org.fleen.geom_2D.polygonRasterMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

/*
 * A polygon's shadow upon the raster cell array
 * all of the cells in which the Polygon manifests as a non-zero intensity Presence
 */
public class PolygonCells{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  PolygonCells(PolygonRasterMap rastermap,DPolygon polygon){
    this.rastermap=rastermap;
    this.polygon=polygon;
    initTransformedPolygon();
    doCells();}
  
  /*
   * ################################
   * RASTER MAP
   * ################################
   */
  
  PolygonRasterMap rastermap;
  
  /*
   * ################################
   * POLYGON
   * ################################
   */
  
  DPolygon 
    polygon,
    transformedpolygon;//x0,y0,x1,y1...
  
  private void initTransformedPolygon(){
    int s=polygon.size();
    transformedpolygon=new DPolygon(s);
    double[] a=new double[2];
    for(DPoint p:polygon){
      a[0]=p.x;
      a[1]=p.y;
      rastermap.transform.transform(a,0,a,0,1);
      transformedpolygon.add(new DPoint(a));}}
  
  /*
   * ################################
   * CELLS
   * ################################
   */
  
  //all locally referred to cells, either from hashmap or created 
  Map<CellKey,Cell> cellcache=new Hashtable<CellKey,Cell>();
  
  //the cells right on the edge-line of the polygon
  Set<Cell> primaryedgecells=new HashSet<Cell>();
  
  List<Set<Cell>>
    //the layers of cells near the edge of the polygon but inside
    edgeinteriorlayers=new ArrayList<Set<Cell>>(),
    //the layers of cells near the edge of the polygon but outside
    edgeexteriorlayers=new ArrayList<Set<Cell>>(),
    //the layers of cells starting just inside the innermost interior edge layer 
    //and ending at the center of the polygon 
    interiorlayers=new ArrayList<Set<Cell>>();

  private void doCells(){
    doPrimaryEdgeCells();
    doOtherEdgeCells();
    doInteriorCells();}
  
  /*
   * first check locally for the cell, then check the rastermap 
   */
  Cell getCell(int x,int y){
    CellKey k=new CellKey(x,y);
    Cell c=cellcache.get(k);
    if(c==null)c=rastermap.getCell(x,y);
    return c;}
  
  private void addCellsToCache(Collection<Cell> cells){
    CellKey k;
    for(Cell c:cells){
      k=new CellKey(c);
      cellcache.put(k,c);}}
  
  /*
   * ################################
   * DO INTERIOR CELLS
   * ################################
   */
  
  private void doInteriorCells(){
    Set<Cell> layer=edgeinteriorlayers.get(edgeinteriorlayers.size()-1);
    while(!layer.isEmpty()){
      layer=getLayerOfUnmarkedCells(layer);
      markInteriorCells(layer);
      interiorlayers.add(layer);
      addCellsToCache(layer);}}
  
  /*
   * ################################
   * DO OTHER EDGE CELLS
   * consider the primaryedgecells. gathered into a list and marked with the polygon's presence.
   * 
   * get all the unmarked neighbors of primaryedgecells
   * mark them, testing for interiorexterior and distance
   * 
   * separate them into interior and exterior
   * 
   * that's the first interior and exterior edge cells layers
   * 
   * Then use each of those layers to derive the next layer, and so on
   * 
   * ################################
   */
  
  private void doOtherEdgeCells(){
    //get the first inner and outer edge cell layers
    Set<Cell> firstinnerouteredgelayer=getLayerOfUnmarkedCells(primaryedgecells);
    markEdgeCells(firstinnerouteredgelayer);
    Set<Cell> 
      inlayer=new HashSet<Cell>(),
      exlayer=new HashSet<Cell>();
    for(Cell c:firstinnerouteredgelayer){
      if(c.getPresenceIntensity(polygon)>0.5)
        inlayer.add(c);
      else
        exlayer.add(c);}
    edgeinteriorlayers.add(inlayer);
    edgeexteriorlayers.add(exlayer);
    //now the first interior and exterior edge layers are done
    //get the number of additional edge layers to do
    int additionaledgelayerscount=(int)(rastermap.glowspan/PolygonRasterMap.CELLSPAN)+1;
    doAdditionalInteriorEdgeLayers(inlayer,additionaledgelayerscount);
    doAdditionalExteriorEdgeLayers(exlayer,additionaledgelayerscount);}
  
  private void doAdditionalInteriorEdgeLayers(Set<Cell> inlayer,int count){
    Set<Cell> layer=inlayer;
    for(int i=0;i<count;i++){
      layer=getLayerOfUnmarkedCells(layer);
      markInteriorEdgeCells(layer);
      edgeinteriorlayers.add(layer);
      addCellsToCache(layer);}}
  
  private void doAdditionalExteriorEdgeLayers(Set<Cell> exlayer,int count){
    Set<Cell> layer=exlayer;
    for(int i=0;i<count;i++){
      layer=getLayerOfUnmarkedCells(layer);
      markExteriorEdgeCells(layer);
      edgeexteriorlayers.add(layer);
      addCellsToCache(layer);}}
  
  /*
   * ################################
   * DO PRIMARY EDGE CELLS
   * 
   * Given a polygon, draw the polygon onto the raster 
   * (using PSEUDOBRESENHAM SUPERCOVER LINE DRAW) 
   * and list the cells that get intersected.
   * ################################
   */
  
  /*
   * do the cells right on the edge-line of the polygon
   */
  private void doPrimaryEdgeCells(){
    int s=polygon.size(),i1;
    Cell c0,c1;
    DPoint p0,p1;
    for(int i0=0;i0<s;i0++){
      //get end points of a side seg of the polygon
      i1=i0+1;
      if(i1==s)i1=0;
      p0=transformedpolygon.get(i0);
      p1=transformedpolygon.get(i1);
      c0=rastermap.getCellContainingPoint(p0.x,p0.y);
      c1=rastermap.getCellContainingPoint(p1.x,p1.y);
      primaryedgecells.addAll(getSegCells(c0.x,c0.y,c1.x,c1.y));}
    markEdgeCells(primaryedgecells);
    addCellsToCache(primaryedgecells);}
  
  /*
   * PSEUDOBRESENHAM SUPERCOVER LINE DRAW
   * 
   * use Bresenham-like algorithm to address a line of squares from (y1,x1) to (y2,x2) 
   * The difference from Bresenham is that ALL the points of the line are 
   * printed, not only one per x coordinate. 
   * Principles of the Bresenham's algorithm (heavily modified) were taken from: 
   * http://www.intranet.ca/~sshah/waste/art7.html 
   */
  List<Cell> getSegCells(int x0,int y0,int x1,int y1){
    List<Cell> segcells=new ArrayList<Cell>();
    int i;               // loop counter 
    int ystep, xstep;    // the step on y and x axis 
    int error;           // the error accumulated during the increment 
    int errorprev;       // vision the previous value of the error variable 
    int y = y0, x = x0;  // the line points 
    double ddy, ddx;        // compulsory variables: the double values of dy and dx 
    int dx = x1 - x0; 
    int dy = y1 - y0;
//    segcells.add(cells[x][y]); //skip the first cell, otherwise some cells get selected twice
    // NB the last point can't be here, because of its previous point (which has to be verified) 
    if (dy < 0){ 
      ystep = -1; 
      dy = -dy; 
    }else 
      ystep = 1; 
    if (dx < 0){ 
      xstep = -1; 
      dx = -dx; 
    }else 
      xstep = 1; 
    ddy = 2 * dy;  // work with double values for full precision 
    ddx = 2 * dx; 
    if (ddx >= ddy){  // first octant (0 <= slope <= 1) 
      // compulsory initialization (even for errorprev, needed when dx==dy) 
      errorprev = error = dx;  // start in the middle of the square 
      for (i=0 ; i < dx ; i++){  // do not use the first point (already done) 
        x += xstep; 
        error += ddy; 
        if (error > ddx){  // increment y if AFTER the middle ( > ) 
          y += ystep; 
          error -= ddx; 
          // three cases (octant == right->right-top for directions below): 
          if (error + errorprev < ddx){  // bottom square also
            segcells.add(rastermap.getCell(x,y-ystep));
          }else if(error + errorprev > ddx){  // left square also 
            segcells.add(rastermap.getCell(x-xstep,y));
          }else{  // corner: bottom and left squares also 
            segcells.add(rastermap.getCell(x,y-ystep));
            segcells.add(rastermap.getCell(x-xstep,y));}} 
        segcells.add(rastermap.getCell(x,y));
        errorprev = error;} 
    }else{// the same as above 
      errorprev = error = dy; 
      for (i=0 ; i < dy ; i++){ 
        y += ystep; 
        error += ddx; 
        if (error > ddy){ 
          x += xstep; 
          error -= ddy; 
          if (error + errorprev < ddy){ 
            segcells.add(rastermap.getCell(x-xstep,y));
          }else if (error + errorprev > ddy){ 
            segcells.add(rastermap.getCell(x,y-ystep));
          }else{ 
            segcells.add(rastermap.getCell(x-xstep,y));
            segcells.add(rastermap.getCell(x,y-ystep));}}
        segcells.add(rastermap.getCell(x,y));
        errorprev = error;}}
    return segcells;}
  
  /*
   * ################################
   * MARK CELLS
   * ################################
   */
  
  /*
   * Given a collection of cells and a polygon and no other
   * useful structure or clues, mark the presence of the polygon
   * upon the cells 
   */
  private void markEdgeCells(Collection<Cell> cells){
    boolean isinterior;
    double dis,presence;
    for(Cell c:cells){
      isinterior=transformedpolygon.containsPoint(c.x,c.y);
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(isinterior)
        presence=0.5+(dis/rastermap.glowspan)*0.5;
      else
        presence=0.5-(dis/rastermap.glowspan)*0.5;
      if(presence<0)presence=0;
      if(presence>1)presence=1;
      c.addPresence(polygon,presence);}}
  
  private void markInteriorEdgeCells(Collection<Cell> cells){
    double dis,presence;
    for(Cell c:cells){
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(dis>rastermap.glowspan)
        presence=1.0;
      else
        presence=0.5+(dis/rastermap.glowspan)*0.5;
      c.addPresence(polygon,presence);}}
  
  private void markExteriorEdgeCells(Collection<Cell> cells){
    double dis,presence;
    for(Cell c:cells){
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(dis>rastermap.glowspan)
        presence=0;
      else
        presence=0.5-(dis/rastermap.glowspan)*0.5;
      c.addPresence(polygon,presence);}}
  
  private void markInteriorCells(Collection<Cell> cells){
    for(Cell c:cells)
      c.addPresence(polygon);}
  
  /*
   * ################################
   * UNMARKED CELL LAYER ACCQUIREMENT
   * 
   * Given a collection of marked (with the presence of the polygon) cells
   * Get all neighbors of those cells that are unmarked with the polygon's presence
   * ################################
   */
  
  Set<Cell> getLayerOfUnmarkedCells(Collection<Cell> cells){
    Set<Cell> unmarkedcells=new HashSet<Cell>();
    for(Cell c:cells)
      for(Cell d:c.getNeighbors(this))
        if(!d.hasPresence(polygon))
          unmarkedcells.add(d);
    return unmarkedcells;}
  
}