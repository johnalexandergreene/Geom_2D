package org.fleen.geom_2D;

public class DHexagon extends DPolygon{

  private static final long serialVersionUID=1173218112060390822L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public DHexagon(DPoint p0,DPoint p1,DPoint p2,DPoint p3,DPoint p4,DPoint p5){
    super(p0,p1,p2,p3,p4,p5);}
  
  public DHexagon(DPolygon p){
    super(p);}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public DPoint getCenter(){
    DPoint p0=get(0),p3=get(3);
    return new DPoint(GD.getPoint_Mid2Points(p0.x,p0.y,p3.x,p3.y));}
  
  public double getRadius(){
    DPoint p0=get(0),p3=get(3);
    return p0.getDistance(p3)/2;}
  
  public double getInnerRadius(){
    return getRadius()*2.0/GD.SQRT3;}

}
