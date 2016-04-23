package org.fleen.geom_2D.polygonRasterMap;

import java.util.Iterator;

class RasterCellIterator implements Iterator<Cell> {
  
  RasterCellIterator(PolygonRasterMap rastermap){
    this.rastermap=rastermap;}
  
  PolygonRasterMap rastermap;
  int x=0,y=0;
  
  public boolean hasNext(){
    return y<rastermap.cellarrayheight;}

  public Cell next(){
    Cell c=rastermap.cells[x][y];
    x++;
    if(x==rastermap.cellarraywidth){
      x=0;
      y++;}
    return c;}

  public void remove(){
    throw new IllegalArgumentException("not implemented");}}
