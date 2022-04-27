package controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import model.Booking;
import model.Business;
import model.Picture;
import model.Review;
import model.Room;
import repository.ReserveDao;
import repository.RoomDao;
import service.ReserveService;
import service.RoomService;
import util.DateParse;

@Controller
@RequestMapping("/room/")
public class RoomController{
	
	private static Map<Object, Object> mainMap = new HashMap<Object, Object>();

	HttpServletRequest request;
	Model model;
	HttpSession session;
	
	private final RoomService roomService;
	private final ReserveService reserveService;
	@Autowired
	public RoomController(RoomService roomService, ReserveService reserveService) {
		super();
		this.roomService = roomService;
		this.reserveService = reserveService;
	}

	@ModelAttribute
	void init(HttpServletRequest request, Model model) {
		this.request = request;
		this.model = model;
		this.session = request.getSession();
	}

	
	// 객실정보 페이지
	
	@RequestMapping("roomlist")
	public String List() {
		List<Room> list = new ArrayList<Room>();
		Map<Object, Object> map = new HashMap<>();
		String bu_email =(String)session.getAttribute("bu_email");
		
		try {
			mainMap = roomService.List(bu_email);
			
			list = (List<Room>) mainMap.get("list");
			map = (Map<Object, Object>) mainMap.get("map");
			
			model.addAttribute("picMap", map);
			model.addAttribute("list", list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/view/entrepreneur/roomlist";
	}
	
	
	// 객실등록 페이지
	@RequestMapping("roomInsert")
	public String roomInsert() {
		return "/view/entrepreneur/roomInsert";
	}
	
	@RequestMapping("roomInsertPro")
	public String roomInsertPro(Room room) {
		String bu_email = (String)session.getAttribute("bu_email");
		String msg = "객실 등록시 오류가 발생했습니다.";
		String url = request.getContextPath() + "/room/roomInsert?bu_email="+bu_email;
		
		try {
			mainMap.clear();
			mainMap = roomService.roomInsertPro(room, bu_email);
			int rnum = (int) mainMap.get("rnum");
			int rowCnt = (int) mainMap.get("rowCnt");
			if(rnum > 0 && rowCnt > 0) {
				msg = "객실 등록이 완료되었습니다.";
				url = request.getContextPath() + "/room/roomlist?bu_email="+bu_email;
			}
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/view/alert";
		
	}
	
	
	@RequestMapping("roominfo")
	public String roominfo(Integer ro_num) {
		
		try {
			mainMap.clear();
			mainMap = roomService.roominfo(ro_num);
			
			model.addAttribute("p_list", mainMap.get("p_list"));
			model.addAttribute("room", mainMap.get("room"));
			model.addAttribute("ro_num", mainMap.get("ro_num"));
			model.addAttribute("pic_num", mainMap.get("pic_num"));
			model.addAttribute("info", mainMap.get("info"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/view/entrepreneur/roominfo";
	}
	
	
	@RequestMapping("roomUpdate")
	public String roomUpdate(Integer ro_num, Integer pic_num) {

		try {
			mainMap.clear();
			mainMap = roomService.roomUpdate(ro_num, pic_num);
			
			model.addAttribute("pic_num", pic_num);
			model.addAttribute("room", mainMap.get("room"));
			model.addAttribute("ro_num", ro_num);
			model.addAttribute("pic", mainMap.get("pic"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/view/entrepreneur/roomUpdate";
	}
	
	
	@RequestMapping("roomUpdatePro")
	public String roomUpdatePro(Room room) {
		
		String bu_email =(String)session.getAttribute("bu_email");
		room.setBu_email(bu_email);
		try {
			mainMap.clear();
			mainMap = roomService.roomUpdatePro(room);
			int rnum = (int) mainMap.get("rnum");
			int pic = (int) mainMap.get("pic");
			
			String msg = "객실 수정시 오류가 발생했습니다.";
			String url = request.getContextPath() + "/room/roomUpdate?ro_num="+room.getRo_num()+"&pic_num="+room.getPic_num();
			
			if(rnum > 0 && pic > 0) {
				msg = "객실 수정이 완료되었습니다.";
				url = request.getContextPath() + "/room/roomlist?bu_email="+bu_email;
			}
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/view/alert";
	}
	
	
	@RequestMapping("roomDelete")
	public String roomDelete(Room room) {
		
		int ro_num = room.getRo_num();
		model.addAttribute("ro_num", ro_num);
		
		return "/view/entrepreneur/roomDelete";
	}
	
	
	@RequestMapping("roomDeletePro")
	public String roomDeletePro(Room r, Business bu) {
		
		String bu_email =(String)session.getAttribute("bu_email");
		try {
			int room = roomService.roomDeltePro(r, bu, bu_email);
			String msg = "객실 삭제시 오류가 발생했습니다.";
			String url = request.getContextPath() + "/room/roomDelete?ro_num="+r.getRo_num();
			
			if(room > 0) {
				msg = "객실 삭제가 완료되었습니다.";
				url = request.getContextPath() + "/room/roomlist?bu_email="+bu_email;
			}
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "/view/alert";
	}
	
//	@RequestMapping("reservation")
//	public String reservation(String pageNum, String searchName, String search) {
//		
//		try {
//			String bu_email =(String)session.getAttribute("bu_email");
//					
//			// 페이지 번호
//			int pageInt;
//			// 한페이지에 출력할 게시글 갯수
//			int limit = 10;
//			
//			// pageNum을 세션에 저장해서 작업후 뒤로가기할때 바로전에 보던 페이지 출력
//			if(pageNum != null){
//				session.setAttribute("pageNum", pageNum);
//			}
//			pageNum = (String) session.getAttribute("pageNum");
//			if(pageNum == null)
//				pageNum = "1";
//					
//			pageInt = Integer.parseInt(pageNum);
//					
////			한페이지에 출력할 게시글 rownum의 번호
//			int startPage = (pageInt-1)*limit + 1;
//			int endPage = (pageInt-1)*limit + limit;
////			게시글 갯수
//			int count = 0;
//			
//			// =========== 현재 시간 ==============
//			LocalDate now = LocalDate.now();
//			// 포맷 정의
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//			// 포맷 적용
//			String nowDay = now.format(formatter);
//			
////			예약 내역 찾기
//			List<Booking> bk = new ArrayList<Booking>();
//			
////			검색할 컬럼이름
//			map.put("searchName", searchName);
////			검색할 컬럼 값
//			map.put("search", search);
//			
//			if("".equals(searchName) || searchName == null || search == null || "".equals(search)) {
//				bk = roomService.selectBkList(map);
//				count = roomService.countBoard(map);
//			}
//			else if("status".equals(searchName)) {
//				if("예약완료".equals(search)) {
//					map.put("status", "1");
//				}
//				else if("결제취소".equals(search)) {
//					map.put("status", "2");
//				}
//				else if("이용완료".equals(search)) {
//					map.put("status", "3");
//				}
//				else if("입실완료".equals(search)) {
//					map.put("status", "4");
//				}
//				else {
//					String msg = "예약완료, 결제취소, 이용완료 , 입실완료중 하나를 입력하세요.";
//					String url = request.getContextPath()+"/room/reservation";
//					model.addAttribute("msg", msg);
//					model.addAttribute("url", url);
//					return "/view/alert";
//				}
//				bk = roomService.searchStatus(map);
//				count = roomService.countBoardStatus(map);
//			}
//			else {
//				bk = roomService.searchName(map);
//				count = roomService.countBoardSearchName(map);
//			}
//			
//			// -----------------------------------------------------------------------------
//			// 게시글 갯수를 확인하는 메서드
//			int boaroomServiceNum = count - (pageInt - 1) *limit;
//			
//			int bottomLine = 3;
//			int startNum = (pageInt - 1) / bottomLine * bottomLine + 1;
//			int endNum = startNum + bottomLine - 1;
//			
//			int maxNum = (count / limit) + (count % limit == 0 ? 0 : 1);
//			if(endNum >= maxNum){
//				endNum = maxNum;
//			}
//			
//			model.addAttribute("search", search);
//			model.addAttribute("searchName", searchName);
//			model.addAttribute("bk", bk);
//			model.addAttribute("boaroomServiceNum", boaroomServiceNum);
//			model.addAttribute("bottomLine", bottomLine);
//			model.addAttribute("startNum", startNum);
//			model.addAttribute("endNum", endNum);
//			model.addAttribute("maxNum", maxNum);
//			model.addAttribute("pageInt", pageInt);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "/view/entrepreneur/reservation";
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("sales")
//	public String sales() {
//
//		try {
//			String bu_email =(String)session.getAttribute("bu_email");
//			String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
//			map.clear();
//			map.put("bu_email", bu_email);
//			JSONArray result = new JSONArray();
//			JSONObject json = new JSONObject();
//			for(String mon : month) {
//				map.put("mon", mon);
//				Booking bo = roomService.selectSales(map);
//				System.out.println("bo : "+bo);
//				if(bo == null) {
//					json.put(mon+"월", "0");
//				}
//				else {
//					json.put(mon+"월", bo.getPrice());
//				}
//			}
//			result.add(json);
//			
//			model.addAttribute("result", result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return "/view/entrepreneur/sales";
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("areaSales")
//	public String areaSales(String month) {
//		
//		map.clear();
//		try {
//			String[] areas = {"서울", "경기", "강원", "부산"};
//			
//			if(month == null) {
//				LocalDate now = LocalDate.now();
//				int month1 = now.getMonthValue();
//				month = "0"+month1;
//			}
//			
//			JSONArray result = new JSONArray();
//			JSONObject json = new JSONObject();
//			
//			map.put("month", month);
//			for(String area : areas) {
//				map.put("area", area);
////				지역별 월매출
//				Booking bo = roomService.selectAreaSales(map);
//				
//				if(bo == null) {
//					json.put(area, "0");
//				}
//				else {
//					json.put(area, bo.getPrice());
//				}
//			}
//			result.add(json);
//			model.addAttribute("month", month);
//			model.addAttribute("result", result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "/view/entrepreneur/areaSales";
//	}
//	
//	@RequestMapping("todayCheckin")
//	public String todayCheckin() {
//		
//		try {
//			String bu_email =(String)session.getAttribute("bu_email");
//			DateParse dp = new DateParse();
//			String checkin = dp.getTodayPlus(0);
//			
//			map.clear();
//			map.put("bu_email", bu_email);
//			map.put("checkin", checkin);
////			아직 체크인 안한 객실내역
//			List<Booking> notCheckin = roomService.selectNotCheckin(map);
////			체크인 완료한 객실내역
//			List<Booking> checkinOk = roomService.selectcheckinOk(map);
//			
//			model.addAttribute("notCheckin", notCheckin);
//			model.addAttribute("checkinOk", checkinOk);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "/view/entrepreneur/todayCheckin";
//	}
//	
//	
//	@RequestMapping("updateTodayCheckin")
//	public String updateTodayCheckin(String bo_num) {
//		
//		try {
//			DateParse dp = new DateParse();
//			String checkin = dp.getTodayPlus(0);
//			String bu_email =(String)session.getAttribute("bu_email");
//			
//			map.clear();
//			map.put("bu_email", bu_email);
//			map.put("bo_num", bo_num);
//			map.put("checkin", checkin);
//			
//			int rowCnt = roomService.updateTodayCheckin(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "redirect:/room/todayCheckin";
//	}
//	
//	
//	@RequestMapping("todayCheckOut")
//	public String todayCheckOut() {
//		
//		try {
//			String bu_email =(String)session.getAttribute("bu_email");
//			DateParse dp = new DateParse();
//			String checkout = dp.getTodayPlus(0);
//			
//			map.clear();
//			map.put("bu_email", bu_email);
//			map.put("checkout", checkout);
////			아직 체크아웃 안한 객실 내역
//			List<Booking> notCheckOut = roomService.selectNotCheckOut(map);
////			체크아웃하고 나간 객실 내역
//			List<Booking> checkOutOk = roomService.selectcheckOutOk(map);
//			
//			
//			model.addAttribute("notCheckOut", notCheckOut);
//			model.addAttribute("checkOutOk", checkOutOk);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "/view/entrepreneur/todayCheckOut";
//	}
//	
//	
//	@RequestMapping("updateTodayCheckOut")
//	public String updateTodayCheckOut(String bo_num) {
//		
//		try {
//			DateParse dp = new DateParse(); 
//			String bu_email =(String)session.getAttribute("bu_email");
//			String checkout = dp.getTodayPlus(0);
//			System.out.println("bo_num"+bo_num);
//			
//			map.clear();
//			map.put("bu_email", bu_email);
//			map.put("bo_num", bo_num);
//			map.put("checkout", checkout);
//			
//			int rowCnt = roomService.updateAndDeleteTodayCheckOut(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "redirect:/room/todayCheckOut";
//	}
//	
//	@RequestMapping("roomReview")
//	public String roomReview(Review re) {
//		
//		try {
//			String bu_email = (String)session.getAttribute("bu_email");
//			
//			if(re.getContent_reply() == null || "".equals(re.getContent_reply()))
//				re.setContent_reply("");
//			
//			List<Review> reviewList = reserveService.businessReviewList(bu_email);
//			model.addAttribute("reviewList", reviewList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "/view/entrepreneur/roomReview";
//	}
	}


