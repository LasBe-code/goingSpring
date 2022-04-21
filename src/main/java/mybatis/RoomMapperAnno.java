package mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import model.Booking;
import model.Business;
import model.Picture;
import model.Reserved;
import model.Room;

public interface RoomMapperAnno {

//	bu_email 에 해당하는 객실 리스트 가져오는데 가격순으로 가져오기 
	@Select("select * from room where bu_email = #{bu_email} order by lpad(ro_price, 10, '0')")
	List<Room> roomList(String bu_email);
	
	//roominfo 슬라이드에 나올 첫번째 사진
	@Select("select substr(location ,0,INSTRB(location,',')-1) location "
			+ "from room r, picture p where r.bu_email = #{bu_email} and r.pic_num = p.pic_num and r.ro_num = #{ro_num}")
	String picMain(Map<String, Object> map);
	
//	사진 등록시 시퀀스 +1
	@Select("select picseq.nextval from dual")
	int nextPicNum();
	
//	객실 등록시 시퀀스 +1
	@Select("select roseq.nextval from dual")
	int nextRoNum();
	
//	객실 등록
	@Insert("insert into room (ro_num, bu_email, ro_count, ro_name, ro_price, checkin, checkout, ro_info, pic_num) "
			+ " values (#{ro_num}, #{bu_email}, #{ro_count}, #{ro_name}, #{ro_price}, #{checkin}, #{checkout}, #{ro_info}, #{pic_num}) ")
	int insertRoom(Room room);

//	객실 사진 등록
	@Insert("insert into picture values(#{pic_num}, #{location})")
	int insertPicture(Picture p);
	
//	ro_num과 일치하는 방 정보 가져오기
	@Select("select * from room where ro_num = #{ro_num}")
	Room selectRoom(int ro_num);
	
//	pic_num과 일치하는 사진리스트 가져오기
	@Select("select * from picture where pic_num = #{pic_num}")
	List<Picture> selectPic(int pic_num);

	
//	객실 정보 수정에서 사진 수정
	@Update("update picture set location = #{location} where pic_num = #{pic_num}")
	int updatePicture(Picture p);

//	객실 정보 수정
	@Update("update room set  bu_email = #{bu_email}, ro_count = #{ro_count}, ro_name = #{ro_name}, ro_price = #{ro_price}, "
			+ " checkin = #{checkin}, checkout = #{checkout}, ro_info = #{ro_info} where ro_num = #{ro_num}")
	int updateRoom(Room room);

//	bu_email , ro_num 과 일치하는 객실 삭제
	@Delete("delete from room where bu_email = #{bu_email} and ro_num = #{ro_num}")
	int deleteRoom(Map<String, Object> map);

//	객실 삭제시 사업자 비밀번호 확인용
	@Select("select bu_password from business where bu_email = #{bu_email} ")
	Business selectBu(String bu_email);

//	검색 안했을때 예약 리스트 테이블의 예약정보
	@Select("select * from (select rownum rnum, a.* "
			+ " from(select m.name, m.tel, m.email,b.ro_name, b.checkin, b.checkout, r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email order by status, checkin) a) "
			+ "	where rnum between #{startPage} and #{endPage}")
	List<Booking> selectBkList(Map<String, Object> map);
	
//	검색 안했을때 게시글 갯수 확인
	@Select("select count(*) from (select rownum rnum, a.* "
			+ "	from(select m.name, m.tel, m.email,b.ro_name, b.checkin, b.checkout, r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email order by status, checkin) a)")
	int countBoard(Map<String, Object> map);
	
	
//	예약상태를 검색할때
	@Select("select * from (select rownum rnum, a.* "
			+ " from(select m.name, m.tel, m.email,b.ro_name, b.checkin, b.checkout, r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email order by status, checkin) a "
			+ "where ${searchName} like #{status}) where rnum between #{startPage} and #{endPage} ")
	List<Booking> searchStatus(Map<String, Object> map);
	
//	예약상태를 검색할때 게시글 갯수
	@Select("select count(*) from (select rownum rnum, a.* "
			+ "	from(select m.name, m.tel, m.email,b.ro_name, b.checkin, b.checkout, r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email order by status, checkin) a) "
			+ "	where ${searchName} like #{status}")
	int countBoardStatus(Map<String, Object> map);

//	예약상태를 제외한 검색
	@Select("select * from (select rownum rnum, a.* "
			+ " from(select m.name, m.tel, m.email,b.ro_name, b.checkin, b.checkout, r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email order by status, checkin) a "
			+ "where  ${searchName} like '%'||#{search}||'%') where rnum between #{startPage} and #{endPage} ")
	List<Booking> searchName(Map<String, Object> map);
	
//	예약상태를 제외한 검색할때 게시글 갯수
	@Select("select count(*) from (select rownum rnum, a.* "
			+ "	from(select m.name, m.tel, m.email,b.ro_name, b.checkin, b.checkout, r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email order by status, checkin) a) "
			+ "	where ${searchName} like '%'||#{search}||'%'")
	int countBoardSearchName(Map<String, Object> map);

//	 roominfo 슬라이드에 나올 사진들(2번부터) 
	@Select("select substr(location ,INSTRB(location,',')+2) location "
			+ " from room r, picture p where r.bu_email = #{bu_email} and "
			+ " r.pic_num = p.pic_num and r.ro_num = #{ro_num}")
	String picList(Map<String, Object> map);

//	bu_email을 가진 사업자 정보 출력
	@Select("select * from business where bu_email = #{bu_email}")
	Business selectBusiness(String bu_email);

//	pic_num의 사진리스트 가져오기
	@Select("select location from picture where pic_num = #{pic_num}")
	List<Picture> selectLocation(int pic_num);

//	사진 삭제
	@Delete("delete from picture where pic_num = #{pic_num}")
	int deleteLocation(int pic_num);


//	사업자 월별 매출
	@Select("select sum(bo.price) price from business bu, booking bo "
			+ " where bu.bu_title = bo.bu_title and bu.bu_email = #{bu_email} and checkin like '____'||#{mon}||'%' ")
	Booking selectSales(Map<String, Object> map);

	
//	지역별 월별 매출
	@Select("select sum(bo.price) price from business bu, booking bo "
			+ "where bu.bu_title = bo.bu_title and bu.bu_address like #{area}||'%' and bo.checkin like '____'||#{month}||'%' ")
	Booking selectAreaSales(Map<String, Object> map);

//	사업자의 주소를 가져와 지도로 출력
	@Select("select * from (select location, bu_address, bu_title, bu_email, "
			+ "	ROW_NUMBER() OVER (PARTITION BY pic_num order by pic_num) AS NUM "
			+ "	FROM (select bu.bu_address, bu.bu_id, bu.bu_title, p.location, p.pic_num,bu.bu_email from  business bu , "
			+ "	picture p where  bu.pic_num = p.pic_num and bu.bu_id = #{bu_id} and bu.bu_address like #{bu_address}||'%')) where num = 1")
	List<Business> addressList(Map<String, Object> map);

	
// 오늘 체크인목록중 아직 입실안한 방
	@Select("select * from (select b.bo_num, m.name, m.tel, b.ro_name, b.checkin, b.checkout,  r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email and b.status = 1) where checkin = #{checkin} ")
	List<Booking> selectNotCheckin(Map<String, Object> map);

	
//	 오늘 체크인목록중 입실 완료한 방
	@Select("select * from (select b.bo_num, m.name, m.tel, b.ro_name, b.checkin, b.checkout,  r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email and b.status = 4) where checkin = #{checkin}")
	List<Booking> selectcheckinOk(Map<String, Object> map);

//	오늘 체크인목록중 체크인 한사람 예약상태 입실완료로 바꾸기
	@Update("update (select status from booking bo, business bu "
			+ " where bo.bu_title = bu.bu_title and bu.bu_email = #{bu_email} and bo_num = #{bo_num} and checkin = #{checkin}) b set b.status = 4")
	int updateTodayCheckin(Map<String, Object> map);

//	오늘이후에 체크아웃하는 방 목록중 퇴실안한 방
	@Select("select * from (select b.bo_num, m.name, m.tel, b.ro_name, b.checkin, b.checkout,  r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email and b.status = 4) where checkout >= #{checkout} order by checkout")
	List<Booking> selectNotCheckOut(Map<String, Object> map);

//	오늘 체크아웃 목록중 퇴실 완료한 방
	@Select("select * from (select b.bo_num, m.name, m.tel, b.ro_name, b.checkin, b.checkout,  r.ro_count, b.status from member m, room r, booking b "
			+ "	where r.bu_email = #{bu_email} and r.ro_name = b.ro_name and m.email = b.email and b.status = 3) where checkout = #{checkout} order by checkout")
	List<Booking> selectcheckOutOk(Map<String, Object> map);

//	오늘 체크아웃하는 방 예약상태 이용완료로바꾸기
	@Update("update (select * from booking bo, business bu where bo.bu_title = bu.bu_title and "
			+ " bu.bu_email = #{bu_email} and status = 4 and checkout >= #{checkout} and bo_num = #{bo_num}) set status = 3")
	int updateTodayCheckOut(Map<String, Object> map);

//	체크아웃을 원래 예약보다 일찍한 고객의 reserved테이블의 예약내역가져오기
	@Select("")
	List<Reserved> reservedList(Map<String, Object> map);


}
