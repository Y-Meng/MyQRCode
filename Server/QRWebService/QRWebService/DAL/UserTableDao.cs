using System;
using System.Data;
using System.Text;
using System.Data.SqlClient;
using Maticsoft.DBUtility;//Please add references
namespace QR.DAL
{
	/// <summary>
	/// 数据访问类:UserTable
	/// </summary>
	public partial class UserTable
	{
		public UserTable()
		{}
		#region  Method

		/// <summary>
		/// 得到最大ID
		/// </summary>
		public int GetMaxId()
		{
		return DbHelperSQL.GetMaxID("User_id", "UserTable"); 
		}

		/// <summary>
		/// 是否存在该记录
		/// </summary>
		public bool Exists(int User_id)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select count(1) from UserTable");
			strSql.Append(" where User_id=@User_id");
			SqlParameter[] parameters = {
					new SqlParameter("@User_id", SqlDbType.Int,4)
			};
			parameters[0].Value = User_id;

			return DbHelperSQL.Exists(strSql.ToString(),parameters);
		}


		/// <summary>
		/// 增加一条数据
		/// </summary>
		public int Add(QR.Model.UserTable model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("insert into UserTable(");
			strSql.Append("UserName,Password,UserType,Status,LastLoginTime,LastExitTime)");
			strSql.Append(" values (");
			strSql.Append("@UserName,@Password,@UserType,@Status,@LastLoginTime,@LastExitTime)");
			strSql.Append(";select @@IDENTITY");
			SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@Password", SqlDbType.NVarChar,50),
					new SqlParameter("@UserType", SqlDbType.NChar,10),
					new SqlParameter("@Status", SqlDbType.NChar,10),
					new SqlParameter("@LastLoginTime", SqlDbType.DateTime),
					new SqlParameter("@LastExitTime", SqlDbType.DateTime)};
			parameters[0].Value = model.UserName;
			parameters[1].Value = model.Password;
			parameters[2].Value = model.UserType;
			parameters[3].Value = model.Status;
			parameters[4].Value = model.LastLoginTime;
			parameters[5].Value = model.LastExitTime;

			object obj = DbHelperSQL.GetSingle(strSql.ToString(),parameters);
			if (obj == null)
			{
				return 0;
			}
			else
			{
				return Convert.ToInt32(obj);
			}
		}
		/// <summary>
		/// 更新一条数据
		/// </summary>
		public bool Update(QR.Model.UserTable model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("update UserTable set ");
			strSql.Append("UserName=@UserName,");
			strSql.Append("Password=@Password,");
			strSql.Append("UserType=@UserType,");
			strSql.Append("Status=@Status,");
			strSql.Append("LastLoginTime=@LastLoginTime,");
			strSql.Append("LastExitTime=@LastExitTime");
			strSql.Append(" where User_id=@User_id");
			SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@Password", SqlDbType.NVarChar,50),
					new SqlParameter("@UserType", SqlDbType.NChar,10),
					new SqlParameter("@Status", SqlDbType.NChar,10),
					new SqlParameter("@LastLoginTime", SqlDbType.DateTime),
					new SqlParameter("@LastExitTime", SqlDbType.DateTime),
					new SqlParameter("@User_id", SqlDbType.Int,4)};
			parameters[0].Value = model.UserName;
			parameters[1].Value = model.Password;
			parameters[2].Value = model.UserType;
			parameters[3].Value = model.Status;
			parameters[4].Value = model.LastLoginTime;
			parameters[5].Value = model.LastExitTime;
			parameters[6].Value = model.User_id;

			int rows=DbHelperSQL.ExecuteSql(strSql.ToString(),parameters);
			if (rows > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		/// <summary>
		/// 删除一条数据
		/// </summary>
		public bool Delete(int User_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from UserTable ");
			strSql.Append(" where User_id=@User_id");
			SqlParameter[] parameters = {
					new SqlParameter("@User_id", SqlDbType.Int,4)
			};
			parameters[0].Value = User_id;

			int rows=DbHelperSQL.ExecuteSql(strSql.ToString(),parameters);
			if (rows > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		/// <summary>
		/// 批量删除数据
		/// </summary>
		public bool DeleteList(string User_idlist )
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from UserTable ");
			strSql.Append(" where User_id in ("+User_idlist + ")  ");
			int rows=DbHelperSQL.ExecuteSql(strSql.ToString());
			if (rows > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}


		/// <summary>
		/// 得到一个对象实体
		/// </summary>
		public QR.Model.UserTable GetModel(int User_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select  top 1 User_id,UserName,Password,UserType,Status,LastLoginTime,LastExitTime from UserTable ");
			strSql.Append(" where User_id=@User_id");
			SqlParameter[] parameters = {
					new SqlParameter("@User_id", SqlDbType.Int,4)
			};
			parameters[0].Value = User_id;

			QR.Model.UserTable model=new QR.Model.UserTable();
			DataSet ds=DbHelperSQL.Query(strSql.ToString(),parameters);
			if(ds.Tables[0].Rows.Count>0)
			{
				if(ds.Tables[0].Rows[0]["User_id"]!=null && ds.Tables[0].Rows[0]["User_id"].ToString()!="")
				{
					model.User_id=int.Parse(ds.Tables[0].Rows[0]["User_id"].ToString());
				}
				if(ds.Tables[0].Rows[0]["UserName"]!=null && ds.Tables[0].Rows[0]["UserName"].ToString()!="")
				{
					model.UserName=ds.Tables[0].Rows[0]["UserName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Password"]!=null && ds.Tables[0].Rows[0]["Password"].ToString()!="")
				{
					model.Password=ds.Tables[0].Rows[0]["Password"].ToString();
				}
				if(ds.Tables[0].Rows[0]["UserType"]!=null && ds.Tables[0].Rows[0]["UserType"].ToString()!="")
				{
					model.UserType=ds.Tables[0].Rows[0]["UserType"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Status"]!=null && ds.Tables[0].Rows[0]["Status"].ToString()!="")
				{
					model.Status=ds.Tables[0].Rows[0]["Status"].ToString();
				}
				if(ds.Tables[0].Rows[0]["LastLoginTime"]!=null && ds.Tables[0].Rows[0]["LastLoginTime"].ToString()!="")
				{
					model.LastLoginTime=DateTime.Parse(ds.Tables[0].Rows[0]["LastLoginTime"].ToString());
				}
				if(ds.Tables[0].Rows[0]["LastExitTime"]!=null && ds.Tables[0].Rows[0]["LastExitTime"].ToString()!="")
				{
					model.LastExitTime=DateTime.Parse(ds.Tables[0].Rows[0]["LastExitTime"].ToString());
				}
				return model;
			}
			else
			{
				return null;
			}
		}

		/// <summary>
		/// 获得数据列表
		/// </summary>
		public DataSet GetList(string strWhere)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select User_id,UserName,Password,UserType,Status,LastLoginTime,LastExitTime ");
			strSql.Append(" FROM UserTable ");
			if(strWhere.Trim()!="")
			{
				strSql.Append(" where "+strWhere);
			}
			return DbHelperSQL.Query(strSql.ToString());
		}

		/// <summary>
		/// 获得前几行数据
		/// </summary>
		public DataSet GetList(int Top,string strWhere,string filedOrder)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select ");
			if(Top>0)
			{
				strSql.Append(" top "+Top.ToString());
			}
			strSql.Append(" User_id,UserName,Password,UserType,Status,LastLoginTime,LastExitTime ");
			strSql.Append(" FROM UserTable ");
			if(strWhere.Trim()!="")
			{
				strSql.Append(" where "+strWhere);
			}
			strSql.Append(" order by " + filedOrder);
			return DbHelperSQL.Query(strSql.ToString());
		}

		/// <summary>
		/// 获取记录总数
		/// </summary>
		public int GetRecordCount(string strWhere)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select count(1) FROM UserTable ");
			if(strWhere.Trim()!="")
			{
				strSql.Append(" where "+strWhere);
			}
			object obj = DbHelperSQL.GetSingle(strSql.ToString());
			if (obj == null)
			{
				return 0;
			}
			else
			{
				return Convert.ToInt32(obj);
			}
		}
		/// <summary>
		/// 分页获取数据列表
		/// </summary>
		public DataSet GetListByPage(string strWhere, string orderby, int startIndex, int endIndex)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("SELECT * FROM ( ");
			strSql.Append(" SELECT ROW_NUMBER() OVER (");
			if (!string.IsNullOrEmpty(orderby.Trim()))
			{
				strSql.Append("order by T." + orderby );
			}
			else
			{
				strSql.Append("order by T.User_id desc");
			}
			strSql.Append(")AS Row, T.*  from UserTable T ");
			if (!string.IsNullOrEmpty(strWhere.Trim()))
			{
				strSql.Append(" WHERE " + strWhere);
			}
			strSql.Append(" ) TT");
			strSql.AppendFormat(" WHERE TT.Row between {0} and {1}", startIndex, endIndex);
			return DbHelperSQL.Query(strSql.ToString());
		}

		/*
		/// <summary>
		/// 分页获取数据列表
		/// </summary>
		public DataSet GetList(int PageSize,int PageIndex,string strWhere)
		{
			SqlParameter[] parameters = {
					new SqlParameter("@tblName", SqlDbType.VarChar, 255),
					new SqlParameter("@fldName", SqlDbType.VarChar, 255),
					new SqlParameter("@PageSize", SqlDbType.Int),
					new SqlParameter("@PageIndex", SqlDbType.Int),
					new SqlParameter("@IsReCount", SqlDbType.Bit),
					new SqlParameter("@OrderType", SqlDbType.Bit),
					new SqlParameter("@strWhere", SqlDbType.VarChar,1000),
					};
			parameters[0].Value = "UserTable";
			parameters[1].Value = "User_id";
			parameters[2].Value = PageSize;
			parameters[3].Value = PageIndex;
			parameters[4].Value = 0;
			parameters[5].Value = 0;
			parameters[6].Value = strWhere;	
			return DbHelperSQL.RunProcedure("UP_GetRecordByPage",parameters,"ds");
		}*/

		#endregion  Method

        public  Model.UserTable getUserByName(string username)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select  top 1 User_id,UserName,Password,UserType,Status,LastLoginTime,LastExitTime from UserTable ");
            strSql.Append(" where UserName=@UserName");
            SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50)
			};
            parameters[0].Value = username;

            QR.Model.UserTable model = new QR.Model.UserTable();
            DataSet ds = DbHelperSQL.Query(strSql.ToString(), parameters);
            if (ds.Tables[0].Rows.Count > 0)
            {
                if (ds.Tables[0].Rows[0]["User_id"] != null && ds.Tables[0].Rows[0]["User_id"].ToString() != "")
                {
                    model.User_id = int.Parse(ds.Tables[0].Rows[0]["User_id"].ToString());
                }
                if (ds.Tables[0].Rows[0]["UserName"] != null && ds.Tables[0].Rows[0]["UserName"].ToString() != "")
                {
                    model.UserName = ds.Tables[0].Rows[0]["UserName"].ToString();
                }
                if (ds.Tables[0].Rows[0]["Password"] != null && ds.Tables[0].Rows[0]["Password"].ToString() != "")
                {
                    model.Password = ds.Tables[0].Rows[0]["Password"].ToString();
                }
                if (ds.Tables[0].Rows[0]["UserType"] != null && ds.Tables[0].Rows[0]["UserType"].ToString() != "")
                {
                    model.UserType = ds.Tables[0].Rows[0]["UserType"].ToString();
                }
                if (ds.Tables[0].Rows[0]["Status"] != null && ds.Tables[0].Rows[0]["Status"].ToString() != "")
                {
                    model.Status = ds.Tables[0].Rows[0]["Status"].ToString();
                }
                if (ds.Tables[0].Rows[0]["LastLoginTime"] != null && ds.Tables[0].Rows[0]["LastLoginTime"].ToString() != "")
                {
                    model.LastLoginTime = DateTime.Parse(ds.Tables[0].Rows[0]["LastLoginTime"].ToString());
                }
                if (ds.Tables[0].Rows[0]["LastExitTime"] != null && ds.Tables[0].Rows[0]["LastExitTime"].ToString() != "")
                {
                    model.LastExitTime = DateTime.Parse(ds.Tables[0].Rows[0]["LastExitTime"].ToString());
                }
                return model;
            }
            else
            {
                return null;
            }
        }
    }
}

