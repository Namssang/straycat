package com.straycat.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.straycat.common.dao.MemberDAO;

@Repository("MemberDAO")
public class MemberDAOImpl implements MemberDAO
{
	@Autowired
	private SqlSession sqlSession;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public <T> T selectOne(String id, Object value)
	{
		
		try
		{
			return sqlSession.selectOne(id, value);
			
		} catch (Exception e)
		{
			logger.error(e.toString());
			
			throw e;
		}
		
	}

	@Override
	public List<Map<String, Object>> selectList(String id, Object value)
	{
		try 
		{
			return sqlSession.selectOne(id);
		} catch (Exception e) 
		{
			logger.error(e.toString());
			
			throw e;
		}
	}

	@Override
	public <T> T selectOne(String id)
	{
		try 
		{
			return sqlSession.selectOne(id);
		} catch (Exception e) 
		{
			logger.error(e.toString());
			
			throw e;
		}
	}
 
}
