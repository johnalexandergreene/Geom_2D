package org.fleen.geom_2D.rasterMap;

/*
 * cell coors wrapped in an object for use in a map
 */
class CellKey{
  
  CellKey(int x,int y){
    
    this.x=x;
    this.y=y;}
  
  CellKey(Cell c){
    
    this.x=c.x;
    this.y=c.y;}
  
  int x,y;
  
  public int hashCode(){
    return x+y*19;}//TODO need bgger prime
  
  public boolean equals(Object a){
    CellKey b=(CellKey)a;
    return b.x==x&&b.y==y;}}
