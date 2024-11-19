package jspMVCMisoShopping.model.dao;

import java.sql.Date;
import java.sql.SQLException;

import jspMVCMisoShopping.model.dto.AuthInfoDTO;
import jspMVCMisoShopping.model.dto.MemberDTO;

public class UserDAO extends DataBaseInfo {
    public AuthInfoDTO loginSelect(String userId) {
        AuthInfoDTO dto = null;
        con = getConnection();
        sql = "SELECT member_id AS user_id, member_pw AS user_pw, "
            + "       member_name AS user_name, member_email AS user_email, 'mem' AS grade, "
            + "       NULL AS emp_num " // 일반 사용자에 대해 empNum은 null
            + "FROM members "
            + "WHERE member_id = ? "
            + "UNION "
            + "SELECT emp_id AS user_id, emp_pw AS user_pw, emp_name AS user_name, emp_email AS user_email, 'emp' AS grade, "
            + "       emp_num AS emp_num " // 직원 번호(empNum) 가져오기
            + "FROM employees "
            + "WHERE emp_id = ?";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dto = new AuthInfoDTO();
                dto.setGrade(rs.getString("grade"));
                dto.setUserEmail(rs.getString("user_email"));
                dto.setUserId(rs.getString("user_id"));
                dto.setUserName(rs.getString("user_name"));
                dto.setUserPw(rs.getString("user_pw"));
                dto.setEmpNum(rs.getString("emp_num")); // 직원 번호(empNum) 설정
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return dto;
    }

    public void userInsert(MemberDTO dto) {
        con = getConnection();
        sql = "INSERT INTO members(MEMBER_NUM, MEMBER_NAME, MEMBER_ID, MEMBER_PW, MEMBER_ADDR, MEMBER_ADDR_DETAIL, "
            + "               MEMBER_POST, MEMBER_REGIST, GENDER, MEMBER_PHONE1, MEMBER_PHONE2, MEMBER_EMAIL, "
            + "               MEMBER_BIRTH, MEMBER_POINT) "
            + "VALUES ((SELECT CONCAT('mem_', NVL(MAX(SUBSTR(MEMBER_NUM, 5)), 100000) + 1) FROM members), "
            + "        ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?, 0)";
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dto.getMemberName());
            pstmt.setString(2, dto.getMemberId());
            pstmt.setString(3, dto.getMemberPw());
            pstmt.setString(4, dto.getMemberAddr());
            pstmt.setString(5, dto.getMemberAddrDetail());    
            pstmt.setString(6, dto.getMemberPost());
            pstmt.setString(7, dto.getGender());
            pstmt.setString(8, dto.getMemberPhone1());
            pstmt.setString(9, dto.getMemberPhone2());
            pstmt.setString(10, dto.getMemberEmail());
            pstmt.setDate(11, new Date(dto.getMemberBirth().getTime()));
            int i = pstmt.executeUpdate();
            System.out.println(i + "개 행이(가) 삽입되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
