package org.fleen.geom_2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * 2D GEOMETRY
 * 
 * Regular planar 2d geometry with trigonometry etc
 * 
 * We use 2 basic geometry systems in this latest version of the fleen
 * --The first system is constrained to 24 directions. We call it D24
 * That is, all segments are parallel to one of 12 axii. The direction from one point to it's 
 * adjacent (in a strand or loop) is always one of our 24 directions.
 * This graphically unifies and enhances as well as simplifying our task of geometry creation.
 * --The second system is the plain ol 2d plane. After we have built a form in D24 we condition it's strands, smoothing
 * curves and/or whatever.
 * Herein are contained various constants and primitive functions for handling geometry in both of our systems. 
 */
public class GD{
  
  //################
  // CONSTANTS
  //################
  
  public static final double 
    PI=3.14159265358979323846,
    PI2=PI*2.0,
    HALFPI=PI/2.0,
    SQRT2=sqrt(2.0),
    SQRT3=sqrt(3.0),
    GOLDEN_RATIO=1.618033988749894;//(1.0+sqrt(5.0))/2.0;
  
  //################
  // MISC
  //################
  
  public static int abs(int a){
    return (a<0)?-a:a;}
  
  public static double abs(double a){
    return (a<0)?-a:a;}
  
  public static double sqrt(double d){
    return StrictMath.sqrt(d);}
  
  /*
   * Returns the factors of the specified integer starting at 1 and ending at the integer itself.
   * If only 2 are returned then it's prime.
   * If only one is returned then i=1 
   */
  public static List<Integer> getFactors(int i){
    List<Integer> factors=new ArrayList<Integer>();
    for(int factor=1;factor<=i;factor++){
      if(i%factor==0){
        factors.add(new Integer(factor));}}
    return factors;}
  
  //################
  // TRIGONOMETRY FUNCTIONS
  //################
  
  public static double sin(double a) {
    return StrictMath.sin(a);}
  
  public static double cos(double a) {
    return StrictMath.cos(a);}
  
  public static double tan(double a) {
    return StrictMath.tan(a);}
  
  public static double atan2(double x,double y){
    return StrictMath.atan2(x,y);}
  
  //################
  // GET DISTANCE
  //################
  
  public static double getDistance_PointPoint(double p0x,double p0y,double p1x,double p1y){
    p0x-=p1x;
    p0y-=p1y;
    return sqrt(p0x*p0x+p0y*p0y);}
  
  public static double getDistance_PointLine(
    double px,double py,double lx0,double ly0,double lx1,double ly1){
    lx1-=lx0;
    ly1-=ly0;
    px-=lx0;
    py-=ly0;
    double dotprod=px*lx1+py*ly1;
    double projlenSq=dotprod*dotprod/(lx1*lx1+ly1*ly1);
    double lenSq=px*px+py*py-projlenSq;
    if(lenSq<0)lenSq=0;
    return sqrt(lenSq);}
  
//  //from stackoverflow
//  public static double getDistance_PointLineZ(double x, double y, double lx0, double ly0, double lx1, double ly1){
//    double A = x - lx0; // position of point rel one end of line
//    double B = y - ly0;
//    double C = lx1 - lx0; // vector along line
//    double D = ly1 - ly0;
//    double E = -D; // orthogonal vector
//    double F = C;
//    double dot = A * E + B * F;
//    double len_sq = E * E + F * F;
//    return Math.abs(dot)/Math.sqrt(len_sq);}
  
  /*
   * use the line dist as the side of 2 right triangles
   * the distance to the seg end points are the 2 hypoteneuses
   * if the third sides sum to the seg length then we have a point-on-seg
   * otherwise it's off the seg so use smallest point dist.
   */
//  public static final double getDistance_PointZSeg(
//    double px,double py,double sx0,double sy0,double sx1,double sy1){
//    double dline=getDistance_PointLine(px,py,sx0,sy0,sx1,sy1);
//    double seglength=getDistance_2Points(sx0,sy0,sx1,sy1);
//    double hyp0=getDistance_2Points(px,py,sx0,sy0);
//    double hyp1=getDistance_2Points(px,py,sx1,sy1);
//    double dlinesqr=dline*dline;
//    double base0=Math.sqrt((hyp0*hyp0)-dlinesqr);
//    double base1=Math.sqrt((hyp1*hyp1)-dlinesqr);
//    double basesum=base0+base1;
//    double err=seglength*0.0001;
//    double basediff=(Math.abs(seglength-basesum));
//    if(basediff<err){
//      return dline;
//    }else{
//      return Math.min(hyp0,hyp1);}}
  
  //TODO
//  public static double getDistance_PointRay(double px,double py,double rpx,double rpy,double rd){
//    throw new UnsupportedOperationException();}
  
  
  //TODO
//  public static double getDistance_2Lines(double l0p0x,double l0p0y,double l0p1x,double l0p1y,
//    double l1p0x,double l1p0y,double l1p1x,double l1p1y){
//    throw new UnsupportedOperationException();}
//  
//  //TODO
//  public static double getDistance_2Segs(double s0p0x,double s0p0y,double s0p1x,double s0p1y,
//    double s1p0x,double s1p0y,double s1p1x,double s1p1y){
//    throw new UnsupportedOperationException();}
  
  //################
  // GET DIRECTION OR ANGLE
  //################
  
  //Returns the direction of p1 relative to p0
  //range of possible directions is [-PI,PI)
//  public static double getDirection_PointPoint(double p0x,double p0y,double p1x,double p1y){
//    p1x-=p0x;
//    p1y-=p0y;
//    return Math.atan2(p1x,p1y);}
  
  /*
   * normalize direction value
   * returns direction value in the range [0,2PI]
   */
  public static double normalizeDirection(double a){
    //reduce giant values
    a=a%(2.0*Math.PI);
    //fix negatives
    if(a<0){a=(2.0*Math.PI)+a;}
    return a;}
  
//  /**
//   * Returns the direction of p1 relative to p0
//   * range of possible directions is [-PI,PI)
//   */
//  public static double getDirection_2Points(double p0x,double p0y,double p1x,double p1y){
//    p1x-=p0x;
//    p1y-=p0y;
//    return Math.atan2(p1x,p1y);}
  
  /**
   * Returns the direction of p1 relative to p0
   * range of possible directions is [0,2PI)
   * TESTED. WORKS PERFECTLY
   */
  public static double getDirection_PointPoint(double p0x,double p0y,double p1x,double p1y){
    p1x-=p0x;
    p1y-=p0y;
    double d=Math.atan2(p1x,p1y);
    if(d<0)d=(2.0*PI)+d;
    return d;}
  
  /**
   * Consider the sequence of points going from south to north: p0,p1,p2
   * Consider the angle on the right.
   * We return the direction (D) in the center of that angle, pointing right.
   *     P2 o
   *       /
   *      / 
   *  P1 o  ---> D
   *      \
   *       \
   *    P0  o
   * 
   */
  public static final double getDirection_3Points(double p0x,double p0y,double p1x,
    double p1y,double p2x,double p2y){
    double d0=getDirection_PointPoint(p1x,p1y,p2x,p2y);
    double d1=getDirection_PointPoint(p1x,p1y,p0x,p0y);
    return getDirection_2Directions(d0,d1);}
  
  /**
   * Consider the points : p0,p1,p2
   * Traversing them p0 -> p1 -> p2, the angle is on the right.
   * TESTED. WORKS PERFECTLY.
   */
  public static double getAngle_3Points(double p0x,double p0y,double p1x,double p1y,
    double p2x,double p2y){
    double d0=getDirection_PointPoint(p1x,p1y,p2x,p2y);
    double d1=getDirection_PointPoint(p1x,p1y,p0x,p0y);
    return getAngle_2Directions(d0,d1);}
  
  /**
   * Consider the arc described when traversing the arc clockwise from direction d0 to d1.
   * We return the direction in the center of that arc. The direction between the 2 directions
   */
  public static double getDirection_2Directions(double d0,double d1){
    double d=0;
    if(d0<=d1){
      d=(d1+d0)/2.0;
    }else{
      double a=PI2-d0+d1;
      d=normalizeDirection(d0+(a/2.0));}
   return d;}
  
  /**
   * sums a direction and an offset and normalizes the result
   */
  public static double getDirection_DirectionOffset(double d,double offset){
    d+=offset;
    d=normalizeDirection(d);
    return d;}
  
  private static final double BETWEENNESSERROR=1.0/65536.0;
//  private static final double BETWEENNESSERROR=1.0/256;
  
  
  /**
   * Returns true if p is between p0 and p1
   * we use an error based on distance(p0,p1)
   */
  public static final boolean isBetween(double px,double py,double p0x,double p0y,double p1x,double p1y){
    double dp0p1=GD.getDistance_PointPoint(p0x,p0y,p1x,p1y);
    double dpp0=GD.getDistance_PointPoint(px,py,p0x,p0y);
    double dpp1=GD.getDistance_PointPoint(px,py,p1x,p1y);
    return (dpp0+dpp1)<dp0p1*(1.0+BETWEENNESSERROR);}
  
  public static void TEST_getDirection_2Directions(){
    System.out.println("---TEST_getDirection_2Directions---");
    double d0=PI*0.5;
    double d1=PI;
    double d2=getDirection_2Directions(d0,d1);
    System.out.println("TEST1:"+d2);
    d2=getDirection_2Directions(d1,d0);
    System.out.println("TEST2:"+d2);
    System.out.println("----------");}
  
  /**
   * The angle spanned by 2 directions.
   * Returns the angle spanned when we traverse the arc clockwise from d0 to d1.
   * @param d0 The first direction.
   * @param d1 The second direction.
   * @return the angle spanned.
   * 
   * TESTED. WORKS PEFECTLY
   */
  public static final double getAngle_2Directions(double d0,double d1){
    double span;
    if(d0==d1){
      span=0;
    }else{
      if(d0<d1){
        span=d1-d0;
      }else{
        span=(Math.PI*2.0)-d0+d1;}}
    return span;}
  
  public static final double getDirectionalDeviation_3Points(
    double p0x,double p0y,double p1x,double p1y,double p2x,double p2y){
    double d0=getDirection_PointPoint(p0x,p0y,p1x,p1y),d1=getDirection_PointPoint(p1x,p1y,p2x,p2y);
    double d=getDeviation_2Directions(d0,d1);
    return d;}
  
  /**
   * Returns the angle by which d1 deviates from d0.
   * If the d1=d0 then we return 0.
   * If d1 is counterclockwise to d0 then we return a negative value.
   * If d1 is clockwise to d0 then we return a positive value.
   * If d1 is the opposite of d0 then we return PI.
   * Returned value ranges [-PI,PI].
   * ex: a direction perpendicular to this one would return -PI/2.0 or PI/2.0.
   */
  public static final double getDeviation_2Directions(double d0,double d1){
    d0=normalizeDirection(d0);
    d1=normalizeDirection(d1);
    double s=getAngle_2Directions(d0,d1);
    double dev;
    if(d0==d1){
      dev=0;}
    if(s<Math.PI){
      dev=s;
    }else{
      dev=-((Math.PI*2.0)-s);}
    return dev;}
  
  /**
   * The absolute value of getDeviation
   */
  public static final double getAbsDeviation_2Directions(double d0,double d1){
    return abs(getDeviation_2Directions(d0,d1));}
  
//  /**
//   * Returns the magnitude of difference between the specified directions.
//   */
//  public static double getDirectionDifference(double d0,double d1){
//    double a0=Math.abs(d0-d1);
//    double a1=(Math.PI*2.0)-a0;
//    double d=Math.min(a0,a1);
//    return d;}
//  
//  /**
//   * Returns the direction of d1 relative to d0.
//   * It returns an angle offset in the range [PI,-PI].
//   * If d1 is clockwise of d0 a positive value is returned, counterclockwise returns a negative.
//   * if d0==d1 then 0 is returned.
//   * d0 and d1 need to be in the normal range [0,2PI] for a valid value to be returned.
//   * We do a clever thing.
//   * We subtract d0 from d1 and normalize the result. So it's like d0 is 0. So it's easy.
//   */
//  public static double getRelativeDirection(double d0,double d1){
//    double dC=normalizeDirection(d1-d0);
//    double r;
//    if(dC<Math.PI){
//      r=dC; 
//    }else{
//      r=-((Math.PI*2.0)-dC);}
//    return r;}
  
//  public static double getDirection_PointSeg(
//    double px,double py,double sp0x,double sp0y,double sp1x,double sp1y){
//    Point2D.Double.
//    
//  }
  
  //################
  // GET POINT
  //################
  
  /**
   * @param x point x
   * @param y point y
   * @param d offset direction
   * @param i offset interval
   * 
   * Returns point(x,y) on zero interval
   */
  public static final double[] getPoint_PointDirectionInterval(double x,double y,double d,double i){
    if(i==0)return new double[]{x,y};
//    if(i<0)d=G2D.normalizeDirection(d+PI);
    double oY=i*Math.cos(d);
    double oX=i*Math.sin(d);
    double[] p={x+oX,y+oY};
    return p;}
  
  /**
   * returns a point between p0 and p1 at i distance from p0 in the direction of p1
   * @param p0x point0 x
   * @param p0y point0 y
   * @param p1x point1 x
   * @param p1y point1 y
   * @param i offset interval
   */
  public static final double[] getPoint_PointPointInterval(
    double p0x,double p0y,double p1x,double p1y,double i){
    double d=getDirection_PointPoint(p0x,p0y,p1x,p1y);
    double oY=i*Math.cos(d);
    double oX=i*Math.sin(d);
    double[] p={p0x+oX,p0y+oY};
    return p;}
  
  //flat up hexagon vertex directions
//  public static final double[]
//    FUH_VDIR={11.0/6.0*PI,1.0/6.0*PI,3.0/6.0*PI,5.0/6.0*PI,7.0/6.0*PI,9.0/6.0*PI};
  
  public static double[] getPoint_Mid2Points(double p0x,double p0y,double p1x,double p1y){
    if(p0x==p1x&&p0y==p1y)return new double[]{p0x,p0y};
    p0x=(p0x+p1x)/2.0;
    p0y=(p0y+p1y)/2.0;
    return new double[]{p0x,p0y};}
  
  public static final double getSlope(double x0,double y0,double x1,double y1){
      double dy=y1-y0;
      double dx=x1-x0;
      return dy/dx;}
  
  public static final double[] getPoint_ClosestOnLineToPoint(double lx0,double ly0,double lx1,double ly1,double px,double py){
      double m=getSlope(lx0,ly0,lx1,ly1);
      double x=(m*m*lx0-m*(ly0-py)+px)/(m*m+1.0);
      double y=(m*m*py-m*(lx0-px)+ly0)/(m*m+1.0);
      return new double[]{x,y};}
  
//  public static final double[] getPoint_ClosestOnSegToPoint(double sx0,double sy0,double sx1,double sy1,double px,double py){
//    double[] lp=getPoint_ClosestOnLineToPoint(sx0,sy0,sx1,sy1,px,py);
//    if(isBetween(lp[0],lp[1],sx0,sy0,sx1,sy1))return lp;
//    double 
//      d0=getDistance_PointPoint(lp[0],lp[1],sx0,sy0),
//      d1=getDistance_PointPoint(lp[0],lp[1],sx1,sy1);
//    if(d0<d1)
//      return new double[]{sx0,sy0};
//    else
//      return new double[]{sx1,sy1};}
  
  
  /*
   * ################################
   * POINT, CLOSEST, ON SEG TO POINT
   * ################################
   */
  
  /**
   * Returns the closest point on the specified seg to the specified point
   * Got this at http://www.java2s.com
   * It's solid solid
   * 
   * @param sx0 seg.p0.x
   * @param sy0 seg.p0.y
   * @param sx1 seg.p1.x
   * @param sy1 seg.p1.y
   * @param px p.x
   * @param py p.y
   * @return the point
   */
  public static final double[] getPoint_ClosestOnSegToPoint(double sx0,double sy0,double sx1,double sy1,double px,double py){
    double xDelta=sx1-sx0;
    double yDelta=sy1-sy0;
    if((xDelta==0)&&(yDelta==0))
      throw new IllegalArgumentException("Segment start equals segment end : ["+sx0+","+sy0+"]-["+sx1+","+sy1+"]");
    double u=((px-sx0)*xDelta+(py-sy0)*yDelta)/(xDelta*xDelta+yDelta*yDelta);
    final double[] closestPoint;
    if(u<0){
      closestPoint=new double[]{sx0,sy0};
    }else if(u>1){
      closestPoint=new double[]{sx1,sy1};
    }else{
      closestPoint=new double[]{sx0+u*xDelta,sy0+u*yDelta};}
    return closestPoint;}

  
  
  
  /**
   * returns the point on the specified seg closest to p
   * returns the point in terms of real offset from seg.p0
   */
  public static final double getPoint_ClosestOnSegToPoint_AsOffset(double sx0,double sy0,double sx1,double sy1,double px,double py){
    double[] a=getPoint_ClosestOnSegToPoint(sx0,sy0,sx1,sy1,px,py);
    double offset=GD.getDistance_PointPoint(sx0,sy0,a[0],a[1]);
    return offset;}
  
  /**
   * Returns a point on the seg p0(x0,y0)-p1(x1,y1)
   * Bias of 0.5 is exactly between them. 
   * Less than 0.5 is closer to p0, Greater is closer to p1.
   * A bias less than 0 or greater than 1 returns an error
   */
  public static final double[] getPoint_Between2Points(double x0,double y0,double x1,double y1,double bias){
    if(bias<0||bias>1.0)
      throw new IllegalArgumentException("INVALID BIAS VALUE! bias="+bias+". Valid range is [0,1]");
    double x=x0*(1.0-bias)+x1*bias;
    double y=y0*(1.0-bias)+y1*bias;
    return new double[]{x,y};}
  
  //################
  // GET ANGLE
  //################
  
  //################
  // GET INTERSECTION
  //################
  
  /*
   * seg intersection test
   * don't get the intersection, just test
   */
  
  public static final boolean testIntersection_2Segs(
    double s0x0,double s0y0,double s0x1,double s0y1,
    double s1x0,double s1y0,double s1x1,double s1y1){
    return java.awt.geom.Line2D.linesIntersect(s0x0,s0y0,s0x1,s0y1,s1x0,s1y0,s1x1,s1y1);}
  
  /*
   * Simple rectangle intersection test
   * returns true if they intersect
   */
  public static final boolean testIntersection_2Rectangles(
    double r0x,double r0y,double r0width,double r0height,
    double r1x,double r1y,double r1width,double r1height){
    return 
      r0x<r1x+r1width&& 
      r0x+r0width>r1x&& 
      r0y<r1y+r1height&& 
      r0y+r0height>r1y;}
  
  //this seems to work fine
  public static final double[] getPoint_2LinesIntersection(
    double l0x0,double l0y0,double l0x1,double l0y1,
    double l1x0,double l1y0,double l1x1,double l1y1){
    double d = (l0x0-l0x1)*(l1y0-l1y1) - (l0y0-l0y1)*(l1x0-l1x1);
    if(d==0)return null;
    double xi=((l1x0-l1x1)*(l0x0*l0y1-l0y0*l0x1)-(l0x0-l0x1)*(l1x0*l1y1-l1y0*l1x1))/d;
    double yi=((l1y0-l1y1)*(l0x0*l0y1-l0y0*l0x1)-(l0y0-l0y1)*(l1x0*l1y1-l1y0*l1x1))/d;
    return new double[]{xi,yi};}
  
  private static final double GETPOINT_2RAYSINTERSECTION_ANGLE_DIFFERENCE_ERROR=PI/4;
  
  /*
   * this works just fine
   * make 2 lines
   * get the intersection of the lines
   * test the direction of the intersection, does it lay in the ray-directions from
   * the ray-points?
   */
  public static final double[] getPoint_2RaysIntersection(double r0x,double r0y,
    double r0d,double r1x,double r1y,double r1d){
    //make lines by creating a second point for each of our ray points
    double[] p0=getPoint_PointDirectionInterval(r0x,r0y,r0d,1.0);
    double[] p1=getPoint_PointDirectionInterval(r1x,r1y,r1d,1.0);
    //get the intersection of those lines
    double[] i=getPoint_2LinesIntersection(r0x,r0y,p0[0],p0[1],r1x,r1y,p1[0],p1[1]);
    //if the lines didn't intersect (parallel) then the rays don't intersect
    if(i==null)return null;
    //does the intersection lay on the ray?
    //test the direction of the intersection relative to the ray's point
    double dir=getDirection_PointPoint(r0x,r0y,i[0],i[1]);
    double dif=getAbsDeviation_2Directions(dir,r0d);
    if(dif>GETPOINT_2RAYSINTERSECTION_ANGLE_DIFFERENCE_ERROR)return null;
    dir=getDirection_PointPoint(r1x,r1y,i[0],i[1]);
    dif=getAbsDeviation_2Directions(dir,r1d);
    if(dif>GETPOINT_2RAYSINTERSECTION_ANGLE_DIFFERENCE_ERROR)return null;
    return i;}
  
  /*
   * make a ray from the seg
   * get 2ray intersection
   * see if the point is between
   * 
   * this does not seem to work TODO
   * ???????
   * 
   */
//  public static final double[] getIntersection_RaySeg(double rx,double ry,double rd,double sp0x,double sp0y,double sp1x,double sp1y){
//    //make a ray from the seg
//    double segraydir=G2D.getDirection_PointPoint(sp0x,sp0y,sp1x,sp1y);
//    //get intersection of 2 rays
//    double[] i=getPoint_2RaysIntersection(rx,ry,rd,sp0x,sp0y,segraydir);
//    if(i==null)return null;
//    boolean b=G2D.isBetween(i[0],i[1],sp0x,sp0y,sp1x,sp1y);
//    if(b)
//      return i;
//    else
//      return null;}
  
  
  /**
   * return index of point in list closest to specified
   */
  public static final int getPoint_Closest(DPoint p,List<DPoint> points){
    double dtest,dclosest=Double.MAX_VALUE;
    int iclosest=0;
    DPoint ptest;
    for(int i=0;i<points.size();i++){
      ptest=points.get(i);
      dtest=p.getDistance(ptest);
      if(dtest<dclosest){
        dclosest=dtest;
        iclosest=i;}}
    return iclosest;}
  
  /*
   * ################################
   * RAY-SEG INTERSECTION
   * we do it with 2 lines
   * ################################
   */
  
  public static final double[] getIntersection_RaySeg(
    double rx,double ry,double rd,double sp0x,double sp0y,double sp1x,double sp1y){
    double[] rp1=GD.getPoint_PointDirectionInterval(rx,ry,rd,1.0);
    return getIntersection_RaySeg(rx,ry,rp1[0],rp1[1],sp0x,sp0y,sp1x,sp1y);}
  
  public static final double[] getIntersection_RaySeg(
    double rp0x,double rp0y,double rp1x,double rp1y,
    double sp0x,double sp0y,double sp1x,double sp1y){
    //get line line intersection
    double[] i=getPoint_2LinesIntersection(rp0x,rp0y,rp1x,rp1y,sp0x,sp0y,sp1x,sp1y);
    //did the lines intersect? If not (they are parallel) then return null.
    if(i==null)return null;
    //is our intersection on the ray? If not then return null.
    double 
      dray=getDirection_PointPoint(rp0x,rp0y,rp1x,rp1y),
      d0=getDirection_PointPoint(rp0x,rp0y,i[0],i[1]);
    if(getDeviation_2Directions(dray,d0)>HALFPI)return null;//halfpi works, right?
    //is our intersection on the seg? If not then return pi
    if(GD.isBetween(i[0],i[1],sp0x,sp0y,sp1x,sp1y));
    //the intersection works
    return i;}
  
  /*
   * ################################
   * INTERSECTION SEG-SEG
   * ################################
   */
  
  /**
   * @param s0p0x seg0 point0 x
   * @param s0p0y seg0 point0 y
   * @param s0p1x seg0 point1 x 
   * @param s0p1y seg0 point1 y
   * @param s1p0x seg1 point0 x
   * @param s1p0y seg1 point0 y
   * @param s1p1x seg1 point1 x
   * @param s1p1y seg1 point1 y
   * @return the point of intersection. Null on nonintersection.
   */
  public static final double[] getIntersection_SegSeg(
    double s0p0x,double s0p0y,double s0p1x,double s0p1y,double s1p0x,double s1p0y,double s1p1x,double s1p1y) {
    double denom=(s1p1y-s1p0y)*(s0p1x-s0p0x)-(s1p1x-s1p0x)*(s0p1y-s0p0y);
    if(denom==0.0)//Lines are parallel.
      return null;
    double ua=((s1p1x-s1p0x)*(s0p0y-s1p0y)-(s1p1y-s1p0y)*(s0p0x-s1p0x))/denom;
    double ub=((s0p1x-s0p0x)*(s0p0y-s1p0y)-(s0p1y-s0p0y)*(s0p0x-s1p0x))/denom;
      if(ua>=0.0&&ua<=1.0&&ub>=0.0&&ub<=1.0)
        return new double[]{(s0p0x+ua*(s0p1x-s0p0x)),(s0p0y+ua*(s0p1y-s0p0y))};
    return null;}
  
  /**
   * If there's an intersection it returns 2 points, if there isn't it returns null.
   * Returns 2 intersection points {x0,y0,x1,y1}
   * Got it from a reputable coder. It works fine.
   */
  public static double[] getIntersection_2Circles(double c0x,double c0y,double c0r,
    double c1x,double c1y,double c1r){
    double a, dx, dy, d, h, rx, ry;
    double p2x, p2y;
    //dx and dy are the vertical and horizontal distances between the circle centers.
    dx=c1x-c0x;
    dy=c1y-c0y;
    //Determine the straight-line distance between the centers.
    d=sqrt((dy*dy)+(dx*dx));
    //nonintersection, the discs don't touch
    if(d>(c0r+c1r))return null;
    //nonintersection, one circle contains the other
    if(d<abs(c0r-c1r))return null;
    //'p2' is the point where the line through the circle
    //intersection points crosses the line between the circle centers.  
    //Determine the distance from point 0 to point 2.
    a=((c0r*c0r)-(c1r*c1r)+(d*d))/(2.0*d);
    //Determine the coordinates of point 2.
    p2x=c0x+(dx*a/d);
    p2y=c0y+(dy*a/d);
    //Determine the distance from point 2 to either of the intersection points.
    h=sqrt((c0r*c0r)-(a*a));
    //Now determine the offsets of the intersection points from point 2.
    rx=-dy*(h/d);
    ry=dx*(h/d);
    /* Determine the absolute intersection points. */
    double i0x=p2x+rx;
    double i0y=p2y+ry;
    double i1x=p2x-rx;
    double i1y=p2y-ry;
    return new double[]{i0x,i0y,i1x,i1y};}
  
  public static final double[] getCentroid2D(double[][] vp){
    double cx=0.0,cy=0.0;
    int inext;
    for(int i=0;i<vp.length;i++){
      inext=i+1;
      if(inext==vp.length)inext=0;
      cx=cx+(vp[i][0]+vp[inext][0])*(vp[i][1]*vp[inext][0]-vp[i][0]*vp[inext][1]);
      cy=cy+(vp[i][1]+vp[inext][1])*(vp[i][1]*vp[inext][0]-vp[i][0]*vp[inext][1]);}
    double area=getAbsArea2D(vp);
    cx/=(6.0* area);
    cy/=(6.0*area);
    double[] centroid2d=new double[]{cx,cy};
    return centroid2d;}
  
  public static final DPoint getPoint_Mean(List<? extends DPoint> points){
    double x=0,y=0;
    for(DPoint p:points){
      x+=p.x;
      y+=p.y;}
    int s=points.size();
    x/=s;
    y/=s;
    return new DPoint(x,y);}
  
  public static final double getPerimeter(double[][] points){
    int i1,s=points.length;
    double perimeter=0;
    for(int i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      perimeter+=GD.getDistance_PointPoint(
        points[i0][0],points[i0][1],points[i1][0],points[i1][1]);}
    return perimeter;}
  
  public static final double getAbsArea2D(double[][] p){
    return Math.abs(getSignedArea2D(p));}
  
  /*
   * TODO convert all these double[] points to DPoints
   */
  public static boolean isClockwise(List<double[]> p){
    double a=getSignedArea2D(p);
    return a<0;}
  
  public static boolean isClockwise(double[][] p){
    double a=getSignedArea2D(p);
    return a<0;}
  
  public static final void makeClockwise(List<double[]> p){
    if(!isClockwise(p))
      Collections.reverse(p);}
  
  /*
   * get the signed area of the polygon
   * if it's negative then it's clockwise
   */
  public static final boolean isClockwiseD(List<? extends DPoint> points){
    double sum=0.0;
    int inext,s=points.size();
    DPoint pi,pinext;
    for(int i=0;i<s;i++){
      inext=i+1;
      if(inext==s)inext=0;
      pi=points.get(i);
      pinext=points.get(inext);
      sum=sum+(pi.x*pinext.y)-(pi.y*pinext.x);}
    double signedarea2d=0.5*sum;
    return signedarea2d<0;}
  
  /**
   * If it's negative then the points are in clockwise order
   * If it's positive then they're counterclockwise
   */
  public static final double getSignedArea2D(List<double[]> vp){
    double[][] a=vp.toArray(new double[vp.size()][]);
    return getSignedArea2D(a);}
  
  public static final double getSignedArea2D(double[][] vp){
    double sum=0.0;
    int inext;
    for(int i=0;i<vp.length;i++){
      inext=i+1;
      if(inext==vp.length)inext=0;
      sum=sum+(vp[i][0]*vp[inext][1])-(vp[i][1]*vp[inext][0]);}
    double signedarea2d=0.5*sum;
    return signedarea2d;}
  
  /**
   * return area of triangle
   * @param p0 point 0
   * @param p1 point 1
   * @param p2 point 2
   * @return area
   */
  public static final double getArea_Triangle(double[] p0,double[] p1,double[] p2){
    double a=Math.abs(
      (p0[0]*(p1[1]-p2[1])+p1[0]*(p2[1]-p0[1])+p2[0]*(p0[1]-p1[1]))/2);
    return a;}
  
  /**
   * return area of triangle
   * @param p0x
   * @param p0y
   * @param p1x
   * @param p1y 
   * @param p2x
   * @param p2y
   * @return area
   */
  public static final double getArea_Triangle(double p0x,double p0y,double p1x,double p1y,double p2x,double p2y){
    double a=
      Math.abs((p0x*(p1y-p2y)+p1x*(p2y-p0y)+p2x*(p0y-p1y))/2);
    return a;}
  
  /**
   * return the point where the specified nonhorizontal, definitely intersecting 
   * segment intersects the specified horizontal line
   */
  public static final double[] getIntersection_SegHLine(double p0x,double p0y,double p1x,double p1y,double hy){
    if(p0x==p1x)return new double[]{p0x,hy};
    double 
      dx=p1x-p0x,
      dy=p1y-p0y,
      slope=dy/dx,
      x=p0x-(p0y-hy)/slope;
    return new double[]{x,hy};}
  
  /**
   * return the point where the specified nonvertical, definitely intersecting 
   * segment intersects the specified vertical line
   * TODO testing was minimal. beware.
   */
  public static final double[] getIntersection_SegVLine(double p0x,double p0y,double p1x,double p1y,double hx){
    if(p0y==p1y)return new double[]{hx,p0y};
    double 
      dx=p1x-p0x,
      dy=p1y-p0y,
      slope=dy/dx,
      y=p0y-(p0x-hx)*slope;
    return new double[]{hx,y};}
  
  public static final double getDistance_PointSeg(
      double point_x,double point_y,double sp0x,double sp0y,double sp1x,double sp1y){
      double px=sp1x-sp0x;
      double py=sp1y-sp0y;
      double something = px*px + py*py;
      double u=((point_x-sp0x)*px+(point_y-sp0y)*py)/something;
      if(u > 1)
        u = 1;
      else if(u < 0)
        u = 0;
      double x = sp0x + u * px;
      double y = sp0y + u * py;
      double dx = x - point_x;
      double dy = y - point_y;
      double dist = Math.sqrt(dx*dx + dy*dy);
      return dist;}
  
  public static final double getDistance_PointCircle(double px,double py,double cx,double cy,double cr){
    double d=getDistance_PointPoint(px,py,cx,cy)-cr;
    return d;}
  
  /*
   * Same as above except it runs a little faster and returns the square of the distance
   */
  public static final double getDistanceSquared_PointSeg(
      double point_x,double point_y,double sp0x,double sp0y,double sp1x,double sp1y){
      double px=sp1x-sp0x;
      double py=sp1y-sp0y;
      double something = px*px + py*py;
      double u=((point_x-sp0x)*px+(point_y-sp0y)*py)/something;
      if(u > 1)
        u = 1;
      else if(u < 0)
        u = 0;
      double x = sp0x + u * px;
      double y = sp0y + u * py;
      double dx = x - point_x;
      double dy = y - point_y;
      double dist = dx*dx + dy*dy;
      return dist;}
  
//  this is the old pointseg distance method. it works guarenteed. maybe keep for testing.
//  public static double getDistance_PointSeg(
//      double px,double py,double sp0x,double sp0y,double sp1x,double sp1y){
//        return java.awt.geom.Line2D.ptSegDist(sp0x,sp0y,sp1x,sp1y,px,py);}
  
  /*
   * return distancesquared from point to polygon
   */
  public static final double getDistanceSq_PointPolygon(double px,double py,double[][] polygon){
    double testsegdistsq,closestsegdistsq=Double.MAX_VALUE;
    int p0,p1,s=polygon.length;
    for(p0=0;p0<s;p0++){
      p1=p0+1;
      if(p1==s)p1=0;
      testsegdistsq=getDistanceSquared_PointSeg(
        px,py,
        polygon[p0][0],
        polygon[p0][1],
        polygon[p1][0],
        polygon[p1][1]);
      if(testsegdistsq<closestsegdistsq)
        closestsegdistsq=testsegdistsq;}
    return closestsegdistsq;}
  
  public static final double getDistance_PointPolygon(double px,double py,double[][] polygon){
    double testsegdistsq,closestsegdistsq=Double.MAX_VALUE;
    int p0,p1,s=polygon.length;
    for(p0=0;p0<s;p0++){
      p1=p0+1;
      if(p1==s)p1=0;
      testsegdistsq=getDistance_PointSeg(
        px,py,
        polygon[p0][0],
        polygon[p0][1],
        polygon[p1][0],
        polygon[p1][1]);
      if(testsegdistsq<closestsegdistsq)
        closestsegdistsq=testsegdistsq;}
    return closestsegdistsq;}
  
  public static final double getDistance_PointPolygon(double px,double py,List<DPoint> polygon){
    double testsegdist,closestsegdist=Double.MAX_VALUE;
    int i0,i1,s=polygon.size();
    DPoint p0,p1;
    for(i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      p0=polygon.get(i0);
      p1=polygon.get(i1);
      testsegdist=getDistance_PointSeg(px,py,p0.x,p0.y,p1.x,p1.y);
      if(testsegdist<closestsegdist)
        closestsegdist=testsegdist;}
    return closestsegdist;}
  
  /*
   * POLYGON INSIDE TEST
   * test point against polygon for inside/outside
   * return true if the point is inside the polygon, false if it's outside.
   * this is high magic
   */
  public static final boolean getSide_PointPolygon(double x,double y,double[][] polygon){
    int i,j=polygon.length-1;
    boolean c=false;
    for(i=0,j=polygon.length-1;i<polygon.length;j=i++){
      if(((polygon[i][1]>y)!=(polygon[j][1]>y))&&
        (x<(polygon[j][0]-polygon[i][0])*(y-polygon[i][1])/(polygon[j][1]-polygon[i][1])+polygon[i][0]))
        c=!c;}
    return c;}
  
  public static final boolean getSide_PointPolygon(double x,double y,List<? extends DPoint> polygon){
    int i,j=polygon.size()-1;
    boolean c=false;
    for(i=0,j=polygon.size()-1;i<polygon.size();j=i++){
      if(((polygon.get(i).y>y)!=(polygon.get(j).y>y))&&
        (x<(polygon.get(j).x-polygon.get(i).x)*(y-polygon.get(i).y)/(polygon.get(j).y-polygon.get(i).y)+polygon.get(i).x))
        c=!c;}
    return c;}
  
//  //-------------------------------------------
//  //TOTALLY GUARENTEED SLOW VERSIONS
//  public static final boolean getSide_PointPolygon(double x,double y,double[][] polygon){
//    Path2D.Double path=new Path2D.Double();
//    path.moveTo(polygon[0][0],polygon[0][1]);
//    double[] p;
//    for(int i=1;i<polygon.length;i++){
//      p=polygon[i];
//      path.lineTo(p[0],p[1]);}
//    path.closePath();
//    return path.contains(x,y);}
//  
//  public static final boolean getSide_PointPolygon(double x,double y,List<? extends DPoint> polygon){
//    Path2D.Double path=new Path2D.Double();
//    DPoint p=polygon.get(0);
//    path.moveTo(p.x,p.y);
//    for(int i=1;i<polygon.size();i++){
//      p=polygon.get(i);
//      path.lineTo(p.x,p.y);}
//    path.closePath();
//    return path.contains(x,y);}
//  
//  //-------------------------------------------
  
  
  /**
   * Return the height of the specified triangle given 3 points
   * @param b0x base point0 x
   * @param b0y base point0 y
   * @param b1x base point1 x
   * @param b1y base point1 y
   * @param ax apex x
   * @param ay apex y
   * @return The height of the triangle; Distance from base to apex.
   */
  public static final double getHeight_Triangle(double b0x,double b0y,double b1x,double b1y,double ax,double ay){
    double a=getArea_Triangle(b0x,b0y,b1x,b1y,ax,ay);
    double b=getDistance_PointPoint(b0x,b0y,b1x,b1y);
    double h=2*a/b;
    return h;}
  
  
  //colinearity as a factor of distance between points
  private static final double COLINEARITYERROR=0.001;
  
  /**
   * returns true if the 3 points are colinear, within error margin
   */
  public static final boolean getColinearity_3Points(double p0x,double p0y,double p1x,double p1y,double p2x,double p2y){
    double 
      dp0p1=GD.getDistance_PointPoint(p0x,p0y,p1x,p1y),
      dp1p2=GD.getDistance_PointPoint(p1x,p1y,p2x,p2y),
      dp0p2=GD.getDistance_PointPoint(p0x,p0y,p2x,p2y);
    double error=Math.abs(dp0p2-(dp0p1+dp1p2));
    boolean colinear=error<COLINEARITYERROR*dp0p2;
    return colinear;}
  
  /*
   * ################################
   * TEST POINT RELATIVE TO LINE
   * ################################
   */
  
  /**
   * returns value>0 if point is on the left of the line
   * value<0 if the point is on the right
   * value==0 if point is on the line
   * 
   *           lp1
   *            o
   *            |
   *    left    |    right
   *            |
   *            o
   *           lp0
   * 
   */
  public static final double getPointLocation_RelativeToLine(
    double px,double py,double lp0x,double lp0y,double lp1x,double lp1y){
    return ((lp1x-lp0x)*(py-lp0y)-(lp1y-lp0y)*(px-lp0x));}
  
  public static final boolean testPoint_IsLeftOfLine(
    double px,double py,double lp0x,double lp0y,double lp1x,double lp1y){
    return getPointLocation_RelativeToLine(px,py,lp0x,lp0y,lp1x,lp1y)>0;}
  
  public static final boolean testPoint_IsRightOfLine(
    double px,double py,double lp0x,double lp0y,double lp1x,double lp1y){
    return getPointLocation_RelativeToLine(px,py,lp0x,lp0y,lp1x,lp1y)<0;}
  
  public static final boolean testPoint_IsOnLine(
    double px,double py,double lp0x,double lp0y,double lp1x,double lp1y){
    return getPointLocation_RelativeToLine(px,py,lp0x,lp0y,lp1x,lp1y)==0;}
  
  //TEST
  public static void main(String[] a){
   
    System.out.println(getPointLocation_RelativeToLine(1,3,0,0,0,5));
    
  
  
  }
  
//private void bresenhamSegDraw(int x0,int y0,int x1,int y1){
//int w=x1-x0;
//int h=y1-y0;
//int dx1=0,dy1=0,dx2=0,dy2=0;
//if(w<0)
//  dx1=-1; 
//else if(w>0)
//  dx1=1;
//if(h<0)
//  dy1=-1; 
//else if(h>0) 
//  dy1=1;
//if(w<0)
//  dx2=-1; 
//else if(w>0) 
//  dx2=1;
//int longest=Math.abs(w);
//int shortest=Math.abs(h);
//if(!(longest>shortest)){
//  longest=Math.abs(h);
//  shortest=Math.abs(w);
//  if(h<0)
//    dy2=-1; 
//  else if(h>0) 
//    dy2=1;
//    dx2=0;}
//int numerator=longest>>1;
//for(int i=0;i<=longest;i++){
//  edgecells.add(new ZCell(x0,y0));
//  numerator+=shortest;
//  if(!(numerator<longest)){
//    numerator-=longest;
//    x0+=dx1;
//    y0+=dy1;
//  }else{
//    x0+=dx2;
//    y0+=dy2;}}}
//
///*
//* BRESENHAM SUPERCOVER LINE DRAW
//* 
//* use Bresenham-like algorithm to address a line of squares from (y1,x1) to (y2,x2) 
//* The difference from Bresenham is that ALL the points of the line are 
//* printed, not only one per x coordinate. 
//* Principles of the Bresenham's algorithm (heavily modified) were taken from: 
//* http://www.intranet.ca/~sshah/waste/art7.html 
//*/
//void bresenhamSupercoverSegDraw(int x0,int y0,int x1,int y1){
//int i;               // loop counter 
//int ystep, xstep;    // the step on y and x axis 
//int error;           // the error accumulated during the increment 
//int errorprev;       // vision the previous value of the error variable 
//int y = y0, x = x0;  // the line points 
//double ddy, ddx;        // compulsory variables: the double values of dy and dx 
//int dx = x1 - x0; 
//int dy = y1 - y0;
////segcells.add(cells[x][y]); //skip the first cell, otherwise some cells get selected twice
//// NB the last point can't be here, because of its previous point (which has to be verified) 
//if (dy < 0){ 
//  ystep = -1; 
//  dy = -dy; 
//}else 
//  ystep = 1; 
//if (dx < 0){ 
//  xstep = -1; 
//  dx = -dx; 
//}else 
//  xstep = 1; 
//ddy = 2 * dy;  // work with double values for full precision 
//ddx = 2 * dx; 
//if (ddx >= ddy){  // first octant (0 <= slope <= 1) 
//  // compulsory initialization (even for errorprev, needed when dx==dy) 
//  errorprev = error = dx;  // start in the middle of the square 
//  for (i=0 ; i < dx ; i++){  // do not use the first point (already done) 
//    x += xstep; 
//    error += ddy; 
//    if (error > ddx){  // increment y if AFTER the middle ( > ) 
//      y += ystep; 
//      error -= ddx; 
//      // three cases (octant == right->right-top for directions below): 
//      if (error + errorprev < ddx){  // bottom square also
//        edgecells.add(new ZCell(x,y-ystep));
//      }else if(error + errorprev > ddx){  // left square also 
//        edgecells.add(new ZCell(x-xstep,y));
//      }else{  // corner: bottom and left squares also 
//        edgecells.add(new ZCell(x,y-ystep));
//        edgecells.add(new ZCell(x-xstep,y));}} 
//    edgecells.add(new ZCell(x,y));
//    errorprev = error;} 
//}else{// the same as above 
//  errorprev = error = dy; 
//  for (i=0 ; i < dy ; i++){ 
//    y += ystep; 
//    error += ddx; 
//    if (error > ddy){ 
//      x += xstep; 
//      error -= ddy; 
//      if (error + errorprev < ddy){ 
//        edgecells.add(new ZCell(x-xstep,y));
//      }else if (error + errorprev > ddy){ 
//        edgecells.add(new ZCell(x,y-ystep));
//      }else{ 
//        edgecells.add(new ZCell(x-xstep,y));
//        edgecells.add(new ZCell(x,y-ystep));}}
//    edgecells.add(new ZCell(x,y));
//    errorprev = error;}}}
  
  
 
  
}
