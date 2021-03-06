package org.fleen.geom_2D;

public class DSeg{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public DSeg(double x0,double y0,double x1,double y1){
    p0=new DPoint(x0,y0);
    p1=new DPoint(x1,y1);}
  
  public DSeg(DPoint p0,DPoint p1){
    this.p0=p0;
    this.p1=p1;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public DPoint p0,p1;
  
  /*
   * returns the direction from p0 to p1
   */
  public double getForeward(){
    return GD.getDirection_PointPoint(p0.x,p0.y,p1.x,p1.y);}
  
  public double getLength(){
    return GD.getDistance_PointPoint(p0.x,p0.y,p1.x,p1.y);}
  
  /*
   * returns the point at the specified offset from p0.
   * foreward is positive, backward is negative.
   * Points on this seg's line but not on this seg may be specified
   */
  public double[] getPointAtRealOffset(double a){
    return GD.getPoint_PointPointInterval(p0.x,p0.y,p1.x,p1.y,a);}
  
  public DPoint getDPointAtRealOffset(double a){
    return new DPoint(GD.getPoint_PointPointInterval(p0.x,p0.y,p1.x,p1.y,a));}
  
  /*
   * returns the point at the specified offset from p0.
   * foreward is positive, backward is negative.
   * Points on this seg's line but not on this seg may be specified
   * offset is in terms of seg length, so 0 returns p0 and 1 returns p1
   */
  public double[] getPointAtProportionalOffset(double a){
    a*=getLength();
    return GD.getPoint_PointPointInterval(p0.x,p0.y,p1.x,p1.y,a);}
  
  public double getDistance(double x,double y){
    return GD.getDistance_PointSeg(x,y,p0.x,p0.y,p1.x,p1.y);}
  
  public DPoint getPoint_Closest(DPoint p){
    double[] c=GD.getPoint_ClosestOnSegToPoint(p0.x,p0.y,p1.x,p1.y,p.x,p.y);
    return new DPoint(c);}
  
  public boolean intersects(DSeg other){
    return GD.testIntersection_2Segs(
      p0.x,p0.y,p1.x,p1.y,
      other.p0.x,other.p0.y,other.p1.x,other.p1.y);}
  
  /*
   * we get distances
   *   the closest point on s.p0 to this 
   *   the closest point on s.p1 to this
   *   the closest point on s to this.p0
   *   the closest point on s to this.p1
   * return the smallest
   * also check for intersection, which is distance=0.
   */
  public double getDistance(DSeg s){
    if(intersects(s))return 0;
    double[] a={ 
      GD.getDistance_PointSeg(s.p0.x,s.p0.y,p0.x,p0.y,p1.x,p1.y),
      GD.getDistance_PointSeg(s.p1.x,s.p1.y,p0.x,p0.y,p1.x,p1.y),
      GD.getDistance_PointSeg(p0.x,p0.y,s.p0.x,s.p0.y,s.p1.x,s.p1.y),
      GD.getDistance_PointSeg(p1.x,p1.y,s.p0.x,s.p0.y,s.p1.x,s.p1.y)};
    double smallest=Double.MAX_VALUE;
    for(double test:a)
      if(test<smallest)
        smallest=test;
    return smallest;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public boolean equals(Object a){
    DSeg b=(DSeg)a;
    return b.p0.x==p0.x&&b.p0.y==p0.y&&b.p1.x==p1.x&&b.p1.y==p1.y;}
  
}
