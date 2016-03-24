package org.fleen.geom_2D;

public class DCircle{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public DCircle(double x,double y,double r){
    this.x=x;
    this.y=y;
    this.r=r;}
  
  public DCircle(double[] c,double r){
    this.x=c[0];
    this.y=c[1];
    this.r=r;}
  
  public DCircle(DPoint c,double r){
    this.x=c.x;
    this.y=c.y;
    this.r=r;}
  
  public DCircle(){}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public double x=0,y=0,r=0;
  
  public double[] getCenter(){
    return new double[]{x,y};}
  
  public double getArea(){
    return GD.PI*r*r;}
  
  public double getCircumference(){
    return GD.PI*2.0*r;}
  
  public double getDistance(double[] p){
    return GD.getDistance_PointCircle(p[0],p[1],x,y,r);}
  
  public boolean contains(double[] p){
    return getDistance(p)<r;}

}
