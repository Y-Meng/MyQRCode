using System;
using System.Data;
using System.Text;
using System.Data.SqlClient;
using Maticsoft.DBUtility;//Please add references
namespace QR.DAL
{
	/// <summary>
	/// 数据访问类:Task
	/// </summary>
	public partial class Task
	{
		public Task()
		{}
		#region  Method

		/// <summary>
		/// 得到最大ID
		/// </summary>
		public int GetMaxId()
		{
		return DbHelperSQL.GetMaxID("TaskID", "Task"); 
		}

		/// <summary>
		/// 是否存在该记录
		/// </summary>
		public bool Exists(int TaskID)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select count(1) from Task");
			strSql.Append(" where TaskID=@TaskID");
			SqlParameter[] parameters = {
					new SqlParameter("@TaskID", SqlDbType.Int,4)
			};
			parameters[0].Value = TaskID;

			return DbHelperSQL.Exists(strSql.ToString(),parameters);
		}


		/// <summary>
		/// 增加一条数据
		/// </summary>
		public int Add(QR.Model.Task model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("insert into Task(");
			strSql.Append("TaskType,UserName,Description,Time,TaskStatus,TaskDetail)");
			strSql.Append(" values (");
			strSql.Append("@TaskType,@UserName,@Description,@Time,@TaskStatus,@TaskDetail)");
			strSql.Append(";select @@IDENTITY");
			SqlParameter[] parameters = {
					new SqlParameter("@TaskType", SqlDbType.NChar,10),
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@Description", SqlDbType.NVarChar,50),
					new SqlParameter("@Time", SqlDbType.DateTime),
					new SqlParameter("@TaskStatus", SqlDbType.VarChar,50),
					new SqlParameter("@TaskDetail", SqlDbType.NVarChar)};
			parameters[0].Value = model.TaskType;
			parameters[1].Value = model.UserName;
			parameters[2].Value = model.Description;
			parameters[3].Value = model.Time;
			parameters[4].Value = model.TaskStatus;
			parameters[5].Value = model.TaskDetail;

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
		public bool Update(QR.Model.Task model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("update Task set ");
			strSql.Append("TaskType=@TaskType,");
			strSql.Append("UserName=@UserName,");
			strSql.Append("Description=@Description,");
			strSql.Append("Time=@Time,");
			strSql.Append("TaskStatus=@TaskStatus,");
			strSql.Append("TaskDetail=@TaskDetail");
			strSql.Append(" where TaskID=@TaskID");
			SqlParameter[] parameters = {
					new SqlParameter("@TaskType", SqlDbType.NChar,10),
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@Description", SqlDbType.NVarChar,50),
					new SqlParameter("@Time", SqlDbType.DateTime),
					new SqlParameter("@TaskStatus", SqlDbType.VarChar,50),
					new SqlParameter("@TaskDetail", SqlDbType.NVarChar),
					new SqlParameter("@TaskID", SqlDbType.Int,4)};
			parameters[0].Value = model.TaskType;
			parameters[1].Value = model.UserName;
			parameters[2].Value = model.Description;
			parameters[3].Value = model.Time;
			parameters[4].Value = model.TaskStatus;
			parameters[5].Value = model.TaskDetail;
			parameters[6].Value = model.TaskID;

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
		public bool Delete(int TaskID)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Task ");
			strSql.Append(" where TaskID=@TaskID");
			SqlParameter[] parameters = {
					new SqlParameter("@TaskID", SqlDbType.Int,4)
			};
			parameters[0].Value = TaskID;

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
		public bool DeleteList(string TaskIDlist )
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Task ");
			strSql.Append(" where TaskID in ("+TaskIDlist + ")  ");
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
		public QR.Model.Task GetModel(int TaskID)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select  top 1 TaskID,TaskType,UserName,Description,Time,TaskStatus,TaskDetail from Task ");
			strSql.Append(" where TaskID=@TaskID");
			SqlParameter[] parameters = {
					new SqlParameter("@TaskID", SqlDbType.Int,4)
			};
			parameters[0].Value = TaskID;

			QR.Model.Task model=new QR.Model.Task();
			DataSet ds=DbHelperSQL.Query(strSql.ToString(),parameters);
			if(ds.Tables[0].Rows.Count>0)
			{
				if(ds.Tables[0].Rows[0]["TaskID"]!=null && ds.Tables[0].Rows[0]["TaskID"].ToString()!="")
				{
					model.TaskID=int.Parse(ds.Tables[0].Rows[0]["TaskID"].ToString());
				}
				if(ds.Tables[0].Rows[0]["TaskType"]!=null && ds.Tables[0].Rows[0]["TaskType"].ToString()!="")
				{
					model.TaskType=ds.Tables[0].Rows[0]["TaskType"].ToString();
				}
				if(ds.Tables[0].Rows[0]["UserName"]!=null && ds.Tables[0].Rows[0]["UserName"].ToString()!="")
				{
					model.UserName=ds.Tables[0].Rows[0]["UserName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Description"]!=null && ds.Tables[0].Rows[0]["Description"].ToString()!="")
				{
					model.Description=ds.Tables[0].Rows[0]["Description"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Time"]!=null && ds.Tables[0].Rows[0]["Time"].ToString()!="")
				{
					model.Time=DateTime.Parse(ds.Tables[0].Rows[0]["Time"].ToString());
				}
				if(ds.Tables[0].Rows[0]["TaskStatus"]!=null && ds.Tables[0].Rows[0]["TaskStatus"].ToString()!="")
				{
					model.TaskStatus=ds.Tables[0].Rows[0]["TaskStatus"].ToString();
				}
				if(ds.Tables[0].Rows[0]["TaskDetail"]!=null && ds.Tables[0].Rows[0]["TaskDetail"].ToString()!="")
				{
					model.TaskDetail=ds.Tables[0].Rows[0]["TaskDetail"].ToString();
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
			strSql.Append("select TaskID,TaskType,UserName,Description,Time,TaskStatus,TaskDetail ");
			strSql.Append(" FROM Task ");
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
			strSql.Append(" TaskID,TaskType,UserName,Description,Time,TaskStatus,TaskDetail ");
			strSql.Append(" FROM Task ");
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
			strSql.Append("select count(1) FROM Task ");
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
				strSql.Append("order by T.TaskID desc");
			}
			strSql.Append(")AS Row, T.*  from Task T ");
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
			parameters[0].Value = "Task";
			parameters[1].Value = "TaskID";
			parameters[2].Value = PageSize;
			parameters[3].Value = PageIndex;
			parameters[4].Value = 0;
			parameters[5].Value = 0;
			parameters[6].Value = strWhere;	
			return DbHelperSQL.RunProcedure("UP_GetRecordByPage",parameters,"ds");
		}*/
        public DataSet QueryNew(string username)
        {
            string sql = "SELECT * FROM Task WHERE UserName='" + username + "'AND TaskStatus='new'";
            return DbHelperSQL.Query(sql);
        }
		#endregion  Method
	}
}

