package com.nasser.poulet.conquest.model;

import java.lang.Math;
import java.util.Random;

public class StartPositions {
	public static int[][] place(State[][] map, int width, int height) {
		
		int _i = width / 2;
		int _j = height / 2;
		
		int _1strandi;
		int _1strandj;
		
		int _2ndrandi;
		int _2ndrandj;
		
		int _3rdrandi;	
		int _3rdrandj;
		
		int d12;
		int d13;
		int d23;
		
		do {
			Random rand = new Random();
			
			do {				
				_1strandi = rand.nextInt((_i-1) - 0 + 1) + 0;
				_1strandj = rand.nextInt((_j-1) - 0 + 1) +0;
			} while(map[_1strandi][_1strandj].getLoyalty().ordinal() != 1);
			
			
			do {
				_2ndrandi = rand.nextInt((_i-1) - 0 + 1) + 0;
				_2ndrandj = rand.nextInt((height-1) - (_j) + 1) + (_j);
			} while(map[_2ndrandi][_2ndrandj].getLoyalty().ordinal() != 1);
			
			do {
				_3rdrandi = rand.nextInt((width-1) - (_i) + 1) + (_i);
				_3rdrandj = rand.nextInt((height-1) - (_j) + 1) + (_j);
			} while(map[_3rdrandi][_3rdrandj].getLoyalty().ordinal() != 1);
			
			//test de distance
			d12 = Math.abs(_2ndrandi-_1strandi) + Math.abs(_2ndrandj-_1strandj);
			d13 = Math.abs(_3rdrandi-_1strandi) + Math.abs(_3rdrandj-_1strandj);
			d23 = Math.abs(_3rdrandi-_2ndrandi) + Math.abs(_3rdrandj-_2ndrandj);
			
		} while(d12 < 5 || d13 < 5 || d23 < 5);
		
		int[] _1strand = new int[2];
		_1strand[0] = _1strandi;
		_1strand[1] = _1strandj;
		int[] _2ndrand = new int[2];
		_2ndrand[0] = _2ndrandi;
		_2ndrand[1] = _2ndrandj;
		int[] _3rdrand = new int[2];
		_3rdrand[0] = _3rdrandi;
		_3rdrand[1] = _3rdrandj;
		
		int[][] pos = new int[3][];
		pos[0] = _1strand;
		pos[1] = _2ndrand;
		pos[2] = _3rdrand;
		
		return pos;
	}
}
