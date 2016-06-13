/**
 * Flink Hands-on
 * Copyright (C) 2015  Felix Neutatz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.tuberlin.dima.flinkhandson.tables;

import de.tuberlin.dima.flinkhandson.Config;
import de.tuberlin.dima.flinkhandson.utils.Utils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.table.TableEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.table.Table;

public class Tables {

	public static class MyResult {
		public String title;
		public Double averageRating;
		public Integer userRatingCount;
	}

	public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		TableEnvironment tableEnv = new TableEnvironment();

		DataSet<Tuple3<Integer, Integer, Double>> rating =
				env.readCsvFile(Config.pathTo("rating.csv"))
						.fieldDelimiter(",")
						.ignoreFirstLine() //ignore schema
						.types(Integer.class, Integer.class, Double.class);

		DataSet<Tuple3<Integer, String, String>> movie =
				env.readCsvFile(Config.pathTo("movie.csv"))
						.fieldDelimiter(",")
						.ignoreFirstLine() //ignore schema
						.types(Integer.class, String.class, String.class);

		// IMPLEMENT THIS STEP


		//Table movieAVG = ...;


		//DataSet<MyResult> result = tableEnv.toDataSet(movieAVG, MyResult.class); //get result in form of a POJO
		//DataSet<Tuple2<String,Double>> resultTuples = result.map(new MyResult2Tuple()); //convert POJO to Tuple2

		//Utils.plot(resultTuples, true, "Average movie rating", "Movies / TV shows", "Average Rating"); //plot a fancy chart :)
	}

	public static class MyResult2Tuple implements MapFunction<MyResult, Tuple2<String,Double>> {
		@Override
		public Tuple2<String,Double> map(MyResult in) {
			return new Tuple2<String,Double>(in.title, in.averageRating);
		}
	}
	
}
