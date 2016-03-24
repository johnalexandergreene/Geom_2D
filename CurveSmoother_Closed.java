package org.fleen.geom_2D;

import java.util.ArrayList;
import java.util.List;

/*
 * SPLIT-TWEAK POLYGON SMOOTHER
 */
public class CurveSmoother_Closed{
  
  public List<double[]> getSmoothedPolygon(List<double[]> polygon,int smoothness){
    List<double[]> p=polygon;
    for(int i=0;i<smoothness;i++)
      p=getSplitTweak(p);
    return p;}
  
  /*
   * the originals are even, the new points are odd
   */
  public List<double[]> getSplitTweak(List<double[]> polygon){
    int oldsize=polygon.size(),newsize=oldsize*2;
    List<double[]> newpolygon=new ArrayList<double[]>(newsize);
    //copy and add midpoints 
    double[] tmp;
    double[] p0,p1,pmid;
    int inext;
    for(int i=0;i<oldsize;i++){
      inext=i+1;
      if(inext==oldsize)inext=0;
      p0=polygon.get(i);
      p1=polygon.get(inext);
      tmp=GD.getPoint_Mid2Points(p0[0],p0[1],p1[0],p1[1]);
      pmid=new double[2];
      pmid[0]=tmp[0];
      pmid[1]=tmp[1];
      newpolygon.add(p0.clone());
      newpolygon.add(pmid);}
    //move old points
    int inew0,inew1;
    double[] pnew0,pnew1,pold;
    for(int iold=0;iold<newsize;iold+=2){
      inew0=iold-1;
      if(inew0<0)inew0=newsize-1;
      inew1=iold+1;
      if(inew1==newsize)inew1=0;
      pold=newpolygon.get(iold);
      pnew0=newpolygon.get(inew0);
      pnew1=newpolygon.get(inew1);
      tmp=GD.getPoint_Mid2Points(pnew0[0],pnew0[1],pnew1[0],pnew1[1]);
      tmp=GD.getPoint_Mid2Points(tmp[0],tmp[1],pold[0],pold[1]);
      pold[0]=tmp[0];
      pold[1]=tmp[1];}
    //
    return newpolygon;}

}
