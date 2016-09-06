/**
 * 
 * PixelFlow | Copyright (C) 2016 Thomas Diewald - http://thomasdiewald.com
 * 
 * A Processing/Java library for high performance GPU-Computing (GLSL).
 * MIT License: https://opensource.org/licenses/MIT
 * 
 */


package com.thomasdiewald.pixelflow.src.filter;


import com.thomasdiewald.pixelflow.src.PixelFlow;
import com.thomasdiewald.pixelflow.src.dwgl.DwGLSLProgram;
import com.thomasdiewald.pixelflow.src.dwgl.DwGLTexture;

import processing.opengl.PGraphics2D;
import processing.opengl.Texture;

public class Luminance {
  
  public PixelFlow context;
  
  public float[] luminance = {0.2989f, 0.5870f, 0.1140f};
//  public float[] luminance = {0.2126f, 0.7152f, 0.0722f};
//  public float[] luminance = {0.3333f, 0.3333f, 0.3333f}; // rgb average
  
  public Luminance(PixelFlow context){
    this.context = context;
  }
  
  public void apply(PGraphics2D src, PGraphics2D dst) {
    Texture tex_src = src.getTexture();
    if(!tex_src.available()) 
      return;
       
    dst.beginDraw();
    context.begin();
    apply(tex_src.glName, dst.width, dst.height);
    context.end("Luminance.apply");
    dst.endDraw();
  }
  
  public void apply(PGraphics2D src, DwGLTexture dst) {
    Texture tex_src = src.getTexture();
    if(!tex_src.available()) 
      return;
       
    context.begin();
    context.beginDraw(dst);
    apply(tex_src.glName, dst.w, dst.h);
    context.endDraw();
    context.end("Luminance.apply");
  }
  
  
  public void apply(DwGLTexture src, DwGLTexture dst) {
    context.begin();
    context.beginDraw(dst);
    apply(src.HANDLE[0], dst.w, dst.h);
    context.endDraw();
    context.end("Luminance.apply");
  }
  
  DwGLSLProgram shader;
  public void apply(int tex_handle, int w, int h){
    if(shader == null) shader = context.createShader(PixelFlow.SHADER_DIR+"Filter/luminance.frag");
    shader.begin();
    shader.uniform2f     ("wh" , w, h);
    shader.uniformTexture("tex", tex_handle);
    shader.uniform3fv("luminance", 1, luminance);
    shader.drawFullScreenQuad(0, 0, w, h);
    shader.end();
  }
  
  
}
