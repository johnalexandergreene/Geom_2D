package org.fleen.geom_2D;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/*
 * get bounding box : testbox
 * get N random points in testbox : testpoints
 * get deepest point, the point in testpoints that is furthest from all sides of the polygon : D
 * create new testbox at 1/2 scale of original testbox, centered on D
 * get N random points in that box. get deepest point
 * repeat 3 or 4 times 
 */
public class IncircleCalculator{
  
//  private static final int SAMPLESIZE=400,SCANCOUNT=6;//unnecessarily thorough?
  private static final int SAMPLESIZE=80,SCANCOUNT=5;
//  private static final int SAMPLESIZE=60,SCANCOUNT=4;
  
  /*
   * ################################
   * STATIC GET INCIRCLE
   * ################################
   */
  
  /**
   * returns incircle for the specified polygon
   * the radius is good, the center point might be a bit arbitrary depending on the polygon
   */
  public static final DCircle getIncircle(double[][] polygonpoints){
    IncircleCalculator irc=new IncircleCalculator(Arrays.asList(polygonpoints));
    return new DCircle(irc.deeppoint[0],irc.deeppoint[1],Math.sqrt(irc.deeppointdepthsquared));}
  
  public static final DCircle getIncircle(List<DPoint> polygonpoints){
    List<double[]> a=new ArrayList<double[]>(polygonpoints.size());
    for(DPoint p:polygonpoints)
      a.add(new double[]{p.x,p.y});
    IncircleCalculator irc=new IncircleCalculator(a);
    return new DCircle(irc.deeppoint[0],irc.deeppoint[1],Math.sqrt(irc.deeppointdepthsquared));}
  
  /*
   * ################################
   * LOCAL DISPOSABLE INSTANCE
   * ################################
   */
  
  private List<double[]> polygonpoints;
  private List<double[]> polygonsegs;
  private Random random=new Random();
  private Path2D.Double polygonpath;
  private double[] deeppoint=new double[2];
  private double 
    scanboxleft,scanboxtop,
    scanboxwidth,scanboxheight,
    deeppointdepthsquared;
  
  private IncircleCalculator(List<double[]> polygonpoints){
    this.polygonpoints=polygonpoints;
    initPolygonPath();
    initPolygonSegs();
    initScanBox();
    gleanDeepPoint();
    for(int i=0;i<SCANCOUNT;i++){
      updateScanBox();
      gleanDeepPoint();}}
  
  private void initScanBox(){
    double xmin=Double.MAX_VALUE,xmax=Double.MIN_VALUE,ymin=xmin,ymax=xmax;
    int s=polygonpoints.size();
    double[] p;
    for(int i=0;i<s;i++){
      p=polygonpoints.get(i);
      if(p[0]<xmin)xmin=p[0];
      if(p[0]>xmax)xmax=p[0];
      if(p[1]<ymin)ymin=p[1];
      if(p[1]>ymax)ymax=p[1];}
    scanboxleft=xmin;
    scanboxtop=ymin;
    scanboxwidth=xmax-xmin;
    scanboxheight=ymax-ymin;}
  
  //center it on deep point, shrink it by half
  private void updateScanBox(){
    scanboxwidth/=2;
    scanboxheight/=2;
    scanboxleft=deeppoint[0]-scanboxwidth/2;
    scanboxtop=deeppoint[1]-scanboxheight/2;}
  
  private void gleanDeepPoint(){
    double[] testpoint=new double[2];
    double testdepth;
    for(int i=0;i<SAMPLESIZE;i++){
      testpoint[0]=random.nextDouble()*scanboxwidth+scanboxleft; 
      testpoint[1]=random.nextDouble()*scanboxheight+scanboxtop; 
      if(polygonpath.contains(testpoint[0],testpoint[1])){
        testdepth=getPointDepth(testpoint);
        if(testdepth>deeppointdepthsquared){
          deeppoint=testpoint;
          testpoint=new double[2];
          deeppointdepthsquared=testdepth;}}}}
  
  //get distances from point to all polygon segs
  //return smallest
  private double getPointDepth(double[] point){
    double dtest,dmin=Double.MAX_VALUE;
    for(double[] seg:polygonsegs){
//      dtest=GD.getDistance_PointSeg(point[0],point[1],seg[0],seg[1],seg[2],seg[3]);
      //dtest should use distance squared, for speed
      dtest=GD.getDistanceSquared_PointSeg(point[0],point[1],seg[0],seg[1],seg[2],seg[3]);
      if(dtest<dmin)
        dmin=dtest;}
    return dmin;}
  
  private void initPolygonPath(){
    polygonpath=new Path2D.Double();
    double[] point=polygonpoints.get(0);
    polygonpath.moveTo(point[0],point[1]);
    int s=polygonpoints.size();
    for(int i=1;i<s;i++){
      point=polygonpoints.get(i);
      polygonpath.lineTo(point[0],point[1]);}
    polygonpath.closePath();}
  
  //segs go x0,y0,x1,y1
  private void initPolygonSegs(){
    polygonsegs=new ArrayList<double[]>();
    double[] p0,p1;
    int s=polygonpoints.size(),i1;
    for(int i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      p0=polygonpoints.get(i0);
      p1=polygonpoints.get(i1);
      polygonsegs.add(new double[]{p0[0],p0[1],p1[0],p1[1]});}}
  
  /*
   * ################################
   * ################################
   * TEST
   * ################################
   * ################################
   */
  
  public static final void main(String[] a){
    test0();}
  
//  private static final double[][] TESTPOLYGON0={
//    {0,1},{1,1},{1,0},{0,0} 
//  };
//  
//  private static final double[][] TESTPOLYGON1={
//    {0,0},{0,3},{2,3},{2,2} ,{1,2},{0,0}
//  };
//  
//  private static final double[][] TESTPOLYGON2={
//    {0,0},{2,3},{4,0}
//  };
  
  private static final double[][] TESTPOLYGON3={
    {0,0},{1,3},{3,3},{5,4},{5,2},{3,2},{5,0}
  };
  
  private static final void test0(){
    System.out.println("TESTING INCIRCLERADIUSCALCULATOR");
    DCircle c=getIncircle(TESTPOLYGON3);
    System.out.println("DEEP POINT= ["+c.x+","+c.y+"]");
    System.out.println("DEEP POINT DEPTH= "+c.r);
    
  }

}
