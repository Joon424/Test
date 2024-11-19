package jspMVCMisoShopping.controller;

import javax.servlet.http.HttpServletRequest;

import jspMVCMisoShopping.model.dao.GoodsDAO;

public class GoodsVisitCountService {
	public void execute(HttpServletRequest request) {
		String goodsnum = request.getParameter("goodsNum");
		GoodsDAO dao = new GoodsDAO();
		dao.visitCount(goodsnum);
	}
}
