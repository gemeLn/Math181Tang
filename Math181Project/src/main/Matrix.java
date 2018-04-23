package main;

import java.util.ArrayList;

public class Matrix {
	double[][] mat;

	public Matrix(double[][] in) {
		mat = in;
	}

	public static double[] solve(double[][] in, double[] sols) {
		in = inverse(in);
		int l = sols.length;
		double[] ret = new double[l];
		for (int r = 0; r < l; r++) {
			for (int i = 0; i < l; i++) {
				ret[r] += sols[i] * in[r][i];
			}
		}
		return ret;
	}

	public static double[][] sub(double[][] mat, int divr, int divc) {
		int w = mat.length;
		double[][] ret = new double[w - 1][w - 1];
		int col2 = 0;
		int row2 = 0;
		for (int row1 = 0; row1 < w; row1++) {
			if (row1 == divr)
				continue;
			for (int col1 = 0; col1 < w; col1++) {
				if (col1 == divc)
					continue;
				ret[row2][col2] = mat[row1][col1];
				col2++;
			}
			col2 = 0;
			row2++;
		}
		return ret;
	}

	public static int det(double[][] in) {
		double ret = 0;
		int s = in.length;
		if (s == 2) {
			return (int) ((in[0][0] * in[1][1]) - (in[0][1] * in[1][0]));
		}
		for (int i = 0; i < s; i++) {
			ret += (in[0][i] * sign(i) * det(sub(in, 0, i)));
		}
		return (int) ret;
	}

	public double det(Matrix in) {
		return det(in.asArray());
	}

	public double det() {
		return det(this);
	}

	public static double[][] cofactors(double[][] in) {
		int s = in.length;
		double[][] ret = new double[s][s];
		for (int r = 0; r < s; r++) {
			for (int c = 0; c < s; c++) {
				ret[r][c] = sign(r, c) * det(sub(in, r, c));
			}
		}
		return ret;
	}

	public double[][] cofactors() {
		return cofactors(mat);
	}

	public static double[][] transpose(double[][] in) {
		int s = in.length;
		double[][] ret = in;
		double temp = 0;
		for (int r = 0; r < s; r++) {
			for (int c = r; c < s; c++) {
				temp = in[r][c];
				in[r][c] = ret[c][r];
				ret[c][r] = temp;
			}
		}
		return ret;

	}

	public double[][] transpose() {
		return transpose(mat);
	}

	public static double[][] inverse(double[][] in) {
		int s = in.length;
		double d = det(in);
		double temp = 0;
		double[][] ret = new double[s][s];
		for (int r = 0; r < s; r++) {
			for (int c = r; c < s; c++) {
				ret[r][c] = sign(r, c) * det(sub(in, r, c)) / d;
				ret[c][r] = sign(r, c) * det(sub(in, c, r)) / d;
				temp = ret[r][c];
				ret[r][c] = ret[c][r];
				ret[c][r] = temp;
			}
		}
		return ret;
	}

	public double[][] inverse() {
		return inverse(mat);
	}

	public static double sign(int i) {
		if (i % 2 == 0)
			return 1.0;
		return -1.0;
	}

	public static double sign(int i, int j) {
		if ((i + j) % 2 == 0)
			return 1.0;
		return -1.0;
	}

	public void print() {
		print(mat);

	}

	public static void print(double[][] mat) {
		int rows = mat.length;
		int cols = mat[0].length;
		for (int r = 0; r < rows; r++) {
			String k = "[";
			for (int c = 0; c < cols; c++) {
				k += (mat[r][c] + (c == cols - 1 ? "" : ","));
			}
			System.out.println(k + (r == rows - 1 ? "]" : "],"));
		}
		System.out.println("");
	}

	public static void print(double[] mat) {
		int l = mat.length;
		String ret = "[";
		for (int i = 0; i < l; i++) {
			ret += mat[i] + ",";
		}
		System.out.println(ret.substring(0, ret.length() - 1) + "]");
	}

	public static void print(ArrayList<Integer> mat) {
		int l = mat.size();
		String ret = "[";
		for (int i = 0; i < l; i++) {
			ret += mat.get(i) + ",";
		}
		System.out.println(ret.substring(0, ret.length() - 1) + "]");
	}

	public double[][] asArray() {
		return mat;
	}
}
