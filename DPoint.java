package org.fleen.geom_2D;

import java.io.Serializable;

public class DPoint implements Serializable{
  
  private static final long serialVersionUID=-329615825957656621L;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public DPoint(){}
  
  public DPoint(double x,double y){
    this.x=x;
    this.y=y;}
  
  public DPoint(double[] xy){
    x=xy[0];
    y=xy[1];}
  
  public DPoint(DPoint p){
    x=p.x;
    y=p.y;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public double x,y;
  
  public void setLocation(double[] coor){
    x=coor[0];
    y=coor[1];}
  
  public void setLocation(DPoint p){
    x=p.x;
    y=p.y;}
  
  public double getDistance(DPoint p){
    return GD.getDistance_PointPoint(x,y,p.x,p.y);}
  
  public double getDistance(double x,double y){
    return GD.getDistance_PointPoint(this.x,this.y,x,y);}
  
  public double getDirection(DPoint p){
    return GD.getDirection_PointPoint(x,y,p.x,p.y);}
  
  public double getDirection(double x,double y){
    return GD.getDirection_PointPoint(this.x,this.y,x,y);}
  
  public DPoint getPoint(double dir,double dis){
    double[] a=GD.getPoint_PointDirectionInterval(x,y,dir,dis);
    return new DPoint(a);}
  
  public void applyVector(DVector vector){
    double[] a=vector.getLocation(this);
    x=a[0];
    y=a[1];}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public int hashCode(){
    return (int)(x+23*y);}
  
  public boolean equals(Object a){
    DPoint b=(DPoint)a;
    return x==b.x&&y==b.y;}
  
  public String toString(){
    return "["+hashCode()+"("+x+","+y+")]";}

}
