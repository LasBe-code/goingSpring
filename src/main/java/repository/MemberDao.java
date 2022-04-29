package repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import model.Business;
import model.Member;
import model.Picture;
import mybatis.MemberMapperAnno;

@Repository
public class MemberDao{
	
	private final SqlSession sqlSession;
	
	@Autowired
	public MemberDao(SqlSession sqlSession) {
		this.sqlSession=sqlSession;
		System.out.println("MemberDao SqlSession On -> "+this.sqlSession);
	}
	
	public int insertMember(Member member) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).insertMember(member);
	}
	
	public int insertBusiness(Business business, int picNum) throws Exception{
		business.setPic_num(picNum);
		return sqlSession.getMapper(MemberMapperAnno.class).insertBusiness(business);
	}
	
	public Member selectMemberOne(String email) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).selectMemberOne(email);
	}
	
	public Business selectBusinessOne(String bu_email) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).selectBusinessOne(bu_email);
	}

	public int memberTelCount(String tel) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).memberTelCount(tel);
	}
	
	public int businessTelCount(String bu_tel) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).businessTelCount(bu_tel);
	}
	
	public int updateBusiness(Business business) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).updateBusiness(business);
	}

	public List<Picture> selectPic(int pic_num) throws Exception{
		return sqlSession.getMapper(MemberMapperAnno.class).selectPic(pic_num);
	}
	

}