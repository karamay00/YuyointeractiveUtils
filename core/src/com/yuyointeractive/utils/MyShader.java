package com.yuyointeractive.utils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MyShader {
	private String vertex = "";
	private String outline = "";

	public ShaderProgram getShader(String shaderName) {
		ShaderProgram.pedantic = false;
		return new ShaderProgram(vertex, outline);
	}
}