package org.fleen.geom_2D;

/*
 * a sequence of 3 points
 * has a couple of segs, directions
 * describes an angle, possibly a triangle
 * 
 *   p0 -> p1 -> p2 
 * 
 * 
 */
public class DTriplet{
  
  public DTriplet(DPoint p0,DPoint p1,DPoint p2){
    this.p0=p0;
    this.p1=p1;
    this.p2=p2;}
  
  DPoint p0,p1,p2;
  
  /*
   * if p0 -> p1 -> p2 describes an up-progression
   * this is the angle on the right 
   */
  public double getRightAngle(){
    return GD.getAngle_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);}

}
