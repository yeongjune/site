package com.base.vo;
 
import java.util.ArrayList;
import java.util.List;

/**
 * 分页封装类
 */
@SuppressWarnings("unused")
public class PageList<T>
{
	/**
	 * 当前页
	 */
	private int currentPage = 1;

	/**
	 * 每页显示的数量
	 */
	private int pageSize;

	/**
	 * 总记录数
	 */
	private Long totalSize = 0L;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 有没有上一页
	 */
	private boolean hasFirst;

	/**
	 * 有没有下一页
	 */
	private boolean hasLast;

	/**
	 * 有没有下一条
	 */
	private boolean hasPrevious;

	/**
	 * 有没有上一条
	 */
	private boolean hasNext;

	/**
	 * 分页显示的每一页
	 */
	private List<Integer> numbers = new ArrayList<Integer>();
	
	/**
	 * 存储分页显示的对象
	 */
	private List<?> list = new ArrayList<Object>();

	public List<?> getList()
	{
		return list;
	}

	public void setList(List<?> list)
	{
		this.list = list;
	}

	/**
	 * @param currentPage 当前页
	 * @param totalSize 总记录数
	 * @param pageSize 每页显示数量
	 */
	public PageList(Integer currentPage, Long totalSize, Integer pageSize)
	{
		if(currentPage == null || currentPage <=0)
		{
			currentPage = 1;
		}
		if(pageSize == null || pageSize <= 0)
		{
			pageSize = 20;
		}
		this.currentPage = currentPage;
		this.totalSize = totalSize;
		this.pageSize = pageSize;
        this.hasPrevious = isHasPrevious();
        this.hasNext = isHasNext();
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public Long getTotalSize()
	{
		return totalSize;
	}

	// 总页数
	public int getTotalPage()
	{
		totalPage = (int) (totalSize / pageSize);
		if ((totalSize % pageSize != 0))
		{
			totalPage++;
		}
		return totalPage;
	}

	// 需不需要显示首页
	public boolean isHasFirst()
	{
		if (currentPage == 1 || this.getTotalPage() == 0)
		{
			return false;
		}
		return true;
	}

	// 需不需要显示尾页
	public boolean isHasLast()
	{
		if (currentPage == totalPage || totalPage == 0)
		{
			return false;
		}
		return true;
	}

	// 是否显示上一页
	public boolean isHasPrevious()
	{
		if (this.isHasFirst())
		{
			return true;
		}
		return false;
	}

	// 是否显示下一页
	public boolean isHasNext()
	{
		if (this.isHasLast())
		{
			return true;
		}
		return false;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public void setTotalSize(Long totalSize)
	{
		this.totalSize = totalSize;
	}

	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}

	public void setHasFirst(boolean hasFirst)
	{
		this.hasFirst = hasFirst;
	}

	public void setHasLast(boolean hasLast)
	{
		this.hasLast = hasLast;
	}

	public void setHasPrevious(boolean hasPrevious)
	{
		this.hasPrevious = hasPrevious;
	}

	public void setHasNext(boolean hasNext)
	{
		this.hasNext = hasNext;
	}

	public List<Integer> getNumbers()
	{
		// 当超过10页  并且当前页大于2的话 
		if (this.getTotalPage() > 10 && this.currentPage > 2)
		{
			
			for (int i = this.currentPage - 2; i < this.currentPage + 8 && i <= this.getTotalPage(); i++)
			{
				numbers.add(i);
			}
		}
		else
		{
			for (int i = 1; i <= this.getTotalPage() && i <= 10; i++)
			{
				numbers.add(i);
			}
		}
		return numbers;
	}

	public void setNumbers(List<Integer> numbers)
	{
		this.numbers = numbers;
	}

}