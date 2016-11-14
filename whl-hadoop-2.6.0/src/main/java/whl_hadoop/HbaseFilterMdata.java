package whl_hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.ColumnRangeFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SkipFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;

public class HbaseFilterMdata {

	public static void main(String [] args) throws IOException{
		Configuration conf=HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "master,slave1,slave2");
		HTable mtable=new HTable(conf,"student".getBytes());
		//列值过滤器的使用
		//singleColumnValueFilterTest(mtable);
		
		//正则比较器
		//RegexStringComparatorTest(mtable);
		
		//子串比较器的使用
		//SubstringComparatorTest(mtable);
		
		//根据列簇筛选
		//FamilyFilterTest(mtable);
		
		//根据列名限定符
		//ColumnQualifierFilterTest(mtable);
		
		//列名前缀限定符过滤器
		//ColumnPrefixQualifierFilterTest(mtable);
		
		//多名前缀限定符过滤器
		//MultiColumnPrefixQualifierFilterTest(mtable);
		
		//列名范围限定符
		//ColumnRangeFilterTest(mtable);
		
		//行范围过滤器
		//RowFilterTest(mtable);
		
		//skipfilter
		//skipfilterTest(mtable);
		
		//FirstKeyOnlyFilter
		FirstKeyOnlyFilterTest(mtable);
	}
	
	//列值过滤器 singleColumnValueFilter
	public static void singleColumnValueFilterTest(HTable table) throws IOException{
		//创建过滤器链，MUST_PASS_ALL表示要满足过滤器链中所有过滤器的要求，MUST_PASS_ONE表示满足一个就可以
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		SingleColumnValueFilter singleFilter1=new SingleColumnValueFilter(
				"info".getBytes(),
				"birthday".getBytes(), 
				CompareOp.EQUAL,"1995-07-15".getBytes()
		); //过滤器的意思是返回birthday这一列的值为“1995-07-15”的行或者birthday为null的行
		filterList.addFilter(singleFilter1);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	
	//RegexStringComparator（正则比较器的使用）比较器的使用
	public static void RegexStringComparatorTest(HTable table) throws IOException{
		//创建过滤器链，MUST_PASS_ALL表示要满足过滤器链中所有过滤器的要求，MUST_PASS_ONE表示满足一个就可以
				FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
				RegexStringComparator rgcomp=new RegexStringComparator("1995.");
				SingleColumnValueFilter singleFilter1=new SingleColumnValueFilter(
						"info".getBytes(),
						"birthday".getBytes(), 
						CompareOp.EQUAL,rgcomp
				); //过滤器的意思是返回birthday为null、birthday与等于其值与“1995.”进行正则比较的行
				filterList.addFilter(singleFilter1);
				Scan s=new Scan();
				s.setFilter(filterList);
				ResultScanner rscan=table.getScanner(s);//rscan是result数组
				for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
					Cell[] cell=r.rawCells();
					int i=0;
					int cellcount=r.rawCells().length;
					System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
					for(i=0;i<cellcount;i++){
						System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
						System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
						System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
						System.out.println("time stamps为："+cell[i].getTimestamp());
					}
					System.out.println();
				}
			    table.close();
	}
	//SubStringComparator比较器(大小写不敏感)
	public static void SubstringComparatorTest(HTable table) throws IOException{
		//创建过滤器链，MUST_PASS_ALL表示要满足过滤器链中所有过滤器的要求，MUST_PASS_ONE表示满足一个就可以
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		SubstringComparator Subcomp=new SubstringComparator("1995-");
		SingleColumnValueFilter singleFilter1=new SingleColumnValueFilter(
				"info".getBytes(),
				"birthday".getBytes(), 
				CompareOp.EQUAL,Subcomp
		); //过滤器的意思是返回birthday为null、birthday与等于其值中包含“1995-”子串的行
		filterList.addFilter(singleFilter1);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * 根据列簇名过来，FamilyFilter
	 */
	public static void FamilyFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		//创建前缀匹配过来器，即字符串的前缀和指定的字符串一样，其中BinaryComparator为字节全匹配比较器
		BinaryPrefixComparator BinPrecomp=new BinaryPrefixComparator("inf".getBytes());
		FamilyFilter familyFilter=new FamilyFilter(CompareOp.EQUAL, BinPrecomp);
		//返回有列簇的前缀有“inf”字符串的列簇的行
		filterList.addFilter(familyFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * 根据列名限定符来筛选
	 */
	public static void ColumnQualifierFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		//创建前缀匹配过来器，即字符串的前缀和指定的字符串一样，其中BinaryComparator为字节全匹配比较器
		BinaryPrefixComparator BinPrecomp=new BinaryPrefixComparator("birthday".getBytes());
		//列名限定符过滤器
		QualifierFilter qualifierFilter=new QualifierFilter(CompareOp.EQUAL, BinPrecomp);
		//返回有列名限定符的前缀是“birthday”字符串的行
		filterList.addFilter(qualifierFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * 列名限定符前缀过滤器
	 */
	public static void ColumnPrefixQualifierFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		//列名前缀限定符过滤器
		ColumnPrefixFilter columnprefixFilter=new ColumnPrefixFilter("bir".getBytes());
		//返回有列名限定符的前缀是“bir”字符串的行
		filterList.addFilter(columnprefixFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * 多个列名限定符前缀过滤器
	 */
	public static void MultiColumnPrefixQualifierFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		byte[][] prefixs=new byte[][]{"city".getBytes(),"univ".getBytes()};
		//列名前缀限定符过滤器
		MultipleColumnPrefixFilter mulolumnprefixFilter=new MultipleColumnPrefixFilter(prefixs);
		//返回有列名限定符的前缀是“city”或者“univ”字符串的行
		filterList.addFilter(mulolumnprefixFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * 列名范围过滤器ColumnRangeFilter,注意不同的列簇可以有相同名字的列名
	 */
	public static void ColumnRangeFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		byte[][] prefixs=new byte[][]{"city".getBytes(),"univ".getBytes()};
		//列名范围过滤器，第一个参数表示最小值，第二个参数表示是否包含最小值，第三个参数表示最大值，第四个参数表示是否包含最大值
		ColumnRangeFilter columnRangeFilter=new ColumnRangeFilter("c".getBytes(),true,"d".getBytes(),true);
		//返回有列名限定符的大于等于“c”，小于等于“d”
		filterList.addFilter(columnRangeFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * 行范围过滤器，RowFilter
	 */
	public static void RowFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		RowFilter rowFilter=new RowFilter(CompareOp.EQUAL,new RegexStringComparator(".*i"));
		filterList.addFilter(rowFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	
	/*
	 * skipfilter,如果列中某一个列不满足条件，则跳过该行
	 */
	public static void skipfilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		SkipFilter skipFilter=new SkipFilter(new ValueFilter(CompareOp.EQUAL, new BinaryComparator("fz".getBytes())));
		filterList.addFilter(skipFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			Cell[] cell=r.rawCells();
			int i=0;
			int cellcount=r.rawCells().length;
			System.out.println("row key为："+new String(CellUtil.cloneRow(cell[i])));
			for(i=0;i<cellcount;i++){
				System.out.println("column family为："+new String(CellUtil.cloneFamily(cell[i])));
				System.out.println("column qualifier为："+new String(CellUtil.cloneQualifier(cell[i])));
				System.out.println("value 为："+new String(CellUtil.cloneValue(cell[i])));
				System.out.println("time stamps为："+cell[i].getTimestamp());
			}
			System.out.println();
		}
	    table.close();
	}
	/*
	 * FirstKeyOnlyFilter 该过滤器只返回每一行中第一个cell的值，可以用来做数量统计
	 */
	public static void FirstKeyOnlyFilterTest(HTable table) throws IOException{
		FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
		FirstKeyOnlyFilter firstKeyOnlyFilter=new FirstKeyOnlyFilter();
		filterList.addFilter(firstKeyOnlyFilter);
		Scan s=new Scan();
		s.setFilter(filterList);
		ResultScanner rscan=table.getScanner(s);//rscan是result数组
		int sum=0;
		for(Result r:rscan){ //result包含cell数组,cell表示该行记录value的个数
			sum++;
		}
	    table.close();
	    System.out.println("共有"+sum+"条记录");
	}
	
}
