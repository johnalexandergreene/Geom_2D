package org.fleen.geom_2D;

import java.util.HashSet;

/*
 * test points for collision
 * that is, test a point against every point in the group
 * if the point is close enough then we add it to the group.
 *   That point is considered part of the collision
 * And then we offer a resolution point, for all the colliding points : the average
 */
@SuppressWarnings("serial")
public class DPointCollisionGroup extends HashSet<DPoint>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  /*
   * init the group with a single point
   * now it's a group of one point
   */
  public DPointCollisionGroup(double collisiondistance,DPoint point){
    this.collisiondistance=collisiondistance;
    add(point);}
  
  /*
   * ################################
   * COLLISION TEST
   * If a point is closer than collisiondistance to any point in this group then that point
   * is in collision with this group
   * TODO pass the collision distance in instead of calculating it for every group
   * ################################
   */
  
  private double collisiondistance;
  
  public boolean collision(DPoint p){
    for(DPoint p0:this)
      if(p0.getDistance(p)<collisiondistance)
        return true;
    return false;}
  
  /*
   * ################################
   * COLLISION RESOLUTION POINT
   * The resolution of a 1..n point collision
   * The average of all participating points
   * A ZPoint
   * ################################
   */
  
  private DPoint resolution=null;
  
  public DPoint getResolution(){
    if(resolution==null)
      initResolution();
    return resolution;}
  
  private void initResolution(){
    //if this group contains just one point then that's the point
    if(size()==1){
      resolution=new DPoint(iterator().next());
      return;}
    //otherwise we do an average
    double xsum=0,ysum=0;
    for(DPoint p:this){
      xsum+=p.x;
      ysum+=p.y;}
    double s=size();
    xsum/=s;
    ysum/=s;
    resolution=new DPoint(xsum,ysum);}

}
