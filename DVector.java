package org.fleen.geom_2D;

import java.util.List;

/*
 * direction and magnitude components
 */
public class DVector{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //zero vector
  public DVector(){}
  
  //sum vector
  public DVector(DVector... v){
    for(DVector a:v)
      add(a);}
  
  public DVector(List<DVector> v){
    for(DVector a:v)
      add(a);}
  
  public DVector(double direction,double magnitude){
    this.direction=direction;
    this.magnitude=magnitude;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public double 
    direction=0,
    magnitude=0;
  
  /*
   * return the location arrived at when vectoring with this vector off the specified point
   */
  public double[] getLocation(DPoint p){
    double[] a=GD.getPoint_PointDirectionInterval(p.x,p.y,direction,magnitude);
    return a;}
  
  public double[] getLocation(double x,double y){
    double[] a=GD.getPoint_PointDirectionInterval(x,y,direction,magnitude);
    return a;}
  
  public double[] getLocation(double[] p){
    double[] a=GD.getPoint_PointDirectionInterval(p[0],p[1],direction,magnitude);
    return a;}
  
  public void move(DPoint p){
    double[] a=getLocation(p);
    p.x=a[0];
    p.y=a[1];}
  
  public static final DVector getSum(DVector... vectors){
    double[] xy={0,0};
    for(DVector v:vectors)
      xy=GD.getPoint_PointDirectionInterval(xy[0],xy[1],v.direction,v.magnitude);
    DVector v=new DVector();
    v.direction=GD.getDirection_PointPoint(0,0,xy[0],xy[1]);
    v.magnitude=GD.getDistance_PointPoint(0,0,xy[0],xy[1]);
    return v;}
  
  /*
   * add the specified vector to this vector
   */
  public void add(DVector v){
    double[] xy={0,0};
    xy=GD.getPoint_PointDirectionInterval(xy[0],xy[1],direction,magnitude);
    xy=GD.getPoint_PointDirectionInterval(xy[0],xy[1],v.direction,v.magnitude);
    direction=GD.getDirection_PointPoint(0,0,xy[0],xy[1]);
    magnitude=GD.getDistance_PointPoint(0,0,xy[0],xy[1]);}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return "["+direction+","+magnitude+"]";}
  
}
