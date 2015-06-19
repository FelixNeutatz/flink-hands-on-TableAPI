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

package de.tuberlin.dima.flinkhandson.utils;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.api.table.Row;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Utils {
	
	public static void plot(DataSet<Row> row, int movieTitleColumnId, int doubleValueColumnId, boolean isSorted, String chartTitle, String xAxisLabel, String yAxisLabel) throws Exception {
		TupleTypeInfo type = new TupleTypeInfo<Tuple2<String,Double>>(BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.DOUBLE_TYPE_INFO);
		List<Tuple2<String,Double>> list = row.map(new Mapper(movieTitleColumnId,doubleValueColumnId)).returns(type).collect();

		HashMap<String,Double> map = new HashMap<String,Double>();
		for (Tuple2<String,Double> tuple : list) {
			map.put(tuple.f0,tuple.f1);
		}
		
		
		if (isSorted) {
			ValueComparator bvc =  new ValueComparator(map);
			TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);

			for (Tuple2<String,Double> tuple : list) {
				map.put(tuple.f0,tuple.f1);
			}
			sorted_map.putAll(map);

			new SingleSeriesBarChart(chartTitle, xAxisLabel, yAxisLabel, Color.RED, sorted_map);
		} else {
			new SingleSeriesBarChart(chartTitle, xAxisLabel, yAxisLabel, Color.RED, map);
		}
		
		
	}

	public static class Mapper implements MapFunction<Row, Tuple2<String,Double>> {
		private int stringColumnId;
		private int doubleColumnId;
		
		public Mapper(int stringColumnId, int doubleColumnId) {
			this.stringColumnId = stringColumnId;
			this.doubleColumnId = doubleColumnId;
		}
		
		@Override
		public Tuple2<String,Double> map(Row in) {
			return new Tuple2<String,Double>((String)in.productElement(stringColumnId), (Double) in.productElement(doubleColumnId));
		}
	}

	public static class ValueComparator implements Comparator<String> {

		Map<String, Double> base;
		public ValueComparator(Map<String, Double> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with equals.    
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}
}
