using System;
using System.Data;
using System.Text;
using System.Data.SqlClient;
using Maticsoft.DBUtility;//Please add references
namespace QR.DAL
{
	/// <summary>
	/// 数据访问类:Emergency
	/// </summary>
	public partial class Emergency
	{
		public Emergency()
		{}
		#region  Method

		/// <summary>
		/// 得到最大ID
		/// </summary>
		public int GetMaxId()
		{
		return DbHelperSQL.GetMaxID("Emergency_id", "Emergency"); 
		}

		/// <summary>
		/// 是否存在该记录
		/// </summary>
		public bool Exists(int Emergency_id)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select count(1) from Emergency");
			strSql.Append(" where Emergency_id=@Emergency_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Emergency_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Emergency_id;

			return DbHelperSQL.Exists(strSql.ToString(),parameters);
		}


		/// <summary>
		/// 增加一条数据
		/// </summary>
		public int Add(QR.Model.Emergency model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("insert into Emergency(");
			strSql.Append("Event_id,UserName,EventStatus,Note,Pic,VedioName,Vedio,AudioName,Audio,Time)");
			strSql.Append(" values (");
			strSql.Append("@Event_id,@UserName,@EventStatus,@Note,@Pic,@VedioName,@Vedio,@AudioName,@Audio,@Time)");
			strSql.Append(";select @@IDENTITY");
			SqlParameter[] parameters = {
					new SqlParameter("@Event_id", SqlDbType.Int,4),
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@EventStatus", SqlDbType.NVarChar,50),
					new SqlParameter("@Note", SqlDbType.NVarChar,50),
					new SqlParameter("@Pic", SqlDbType.Image),
					new SqlParameter("@VedioName", SqlDbType.NVarChar,50),
					new SqlParameter("@Vedio", SqlDbType.VarBinary),
					new SqlParameter("@AudioName", SqlDbType.NVarChar,50),
					new SqlParameter("@Audio", SqlDbType.VarBinary),
					new SqlParameter("@Time", SqlDbType.DateTime)};
			parameters[0].Value = model.Event_id;
			parameters[1].Value = model.UserName;
			parameters[2].Value = model.EventStatus;
			parameters[3].Value = model.Note;
			parameters[4].Value = model.Pic;
			parameters[5].Value = model.VedioName;
			parameters[6].Value = model.Vedio;
			parameters[7].Value = model.AudioName;
			parameters[8].Value = model.Audio;
			parameters[9].Value = model.Time;

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
		public bool Update(QR.Model.Emergency model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("update Emergency set ");
			strSql.Append("Event_id=@Event_id,");
			strSql.Append("UserName=@UserName,");
			strSql.Append("EventStatus=@EventStatus,");
			strSql.Append("Note=@Note,");
			strSql.Append("Pic=@Pic,");
			strSql.Append("VedioName=@VedioName,");
			strSql.Append("Vedio=@Vedio,");
			strSql.Append("AudioName=@AudioName,");
			strSql.Append("Audio=@Audio,");
			strSql.Append("Time=@Time");
			strSql.Append(" where Emergency_id=@Emergency_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Event_id", SqlDbType.Int,4),
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@EventStatus", SqlDbType.NVarChar,50),
					new SqlParameter("@Note", SqlDbType.NVarChar,50),
					new SqlParameter("@Pic", SqlDbType.Image),
					new SqlParameter("@VedioName", SqlDbType.NVarChar,50),
					new SqlParameter("@Vedio", SqlDbType.VarBinary),
					new SqlParameter("@AudioName", SqlDbType.NVarChar,50),
					new SqlParameter("@Audio", SqlDbType.VarBinary),
					new SqlParameter("@Time", SqlDbType.DateTime),
					new SqlParameter("@Emergency_id", SqlDbType.Int,4)};
			parameters[0].Value = model.Event_id;
			parameters[1].Value = model.UserName;
			parameters[2].Value = model.EventStatus;
			parameters[3].Value = model.Note;
			parameters[4].Value = model.Pic;
			parameters[5].Value = model.VedioName;
			parameters[6].Value = model.Vedio;
			parameters[7].Value = model.AudioName;
			parameters[8].Value = model.Audio;
			parameters[9].Value = model.Time;
			parameters[10].Value = model.Emergency_id;

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
		public bool Delete(int Emergency_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Emergency ");
			strSql.Append(" where Emergency_id=@Emergency_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Emergency_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Emergency_id;

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
		public bool DeleteList(string Emergency_idlist )
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Emergency ");
			strSql.Append(" where Emergency_id in ("+Emergency_idlist + ")  ");
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
		public QR.Model.Emergency GetModel(int Emergency_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select  top 1 Emergency_id,Event_id,UserName,EventStatus,Note,Pic,VedioName,Vedio,AudioName,Audio,Time from Emergency ");
			strSql.Append(" where Emergency_id=@Emergency_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Emergency_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Emergency_id;

			QR.Model.Emergency model=new QR.Model.Emergency();
			DataSet ds=DbHelperSQL.Query(strSql.ToString(),parameters);
			if(ds.Tables[0].Rows.Count>0)
			{
				if(ds.Tables[0].Rows[0]["Emergency_id"]!=null && ds.Tables[0].Rows[0]["Emergency_id"].ToString()!="")
				{
					model.Emergency_id=int.Parse(ds.Tables[0].Rows[0]["Emergency_id"].ToString());
				}
				if(ds.Tables[0].Rows[0]["Event_id"]!=null && ds.Tables[0].Rows[0]["Event_id"].ToString()!="")
				{
					model.Event_id=int.Parse(ds.Tables[0].Rows[0]["Event_id"].ToString());
				}
				if(ds.Tables[0].Rows[0]["UserName"]!=null && ds.Tables[0].Rows[0]["UserName"].ToString()!="")
				{
					model.UserName=ds.Tables[0].Rows[0]["UserName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["EventStatus"]!=null && ds.Tables[0].Rows[0]["EventStatus"].ToString()!="")
				{
					model.EventStatus=ds.Tables[0].Rows[0]["EventStatus"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Note"]!=null && ds.Tables[0].Rows[0]["Note"].ToString()!="")
				{
					model.Note=ds.Tables[0].Rows[0]["Note"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Pic"]!=null && ds.Tables[0].Rows[0]["Pic"].ToString()!="")
				{
					model.Pic=(byte[])ds.Tables[0].Rows[0]["Pic"];
				}
				if(ds.Tables[0].Rows[0]["VedioName"]!=null && ds.Tables[0].Rows[0]["VedioName"].ToString()!="")
				{
					model.VedioName=ds.Tables[0].Rows[0]["VedioName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Vedio"]!=null && ds.Tables[0].Rows[0]["Vedio"].ToString()!="")
				{
					model.Vedio=(byte[])ds.Tables[0].Rows[0]["Vedio"];
				}
				if(ds.Tables[0].Rows[0]["AudioName"]!=null && ds.Tables[0].Rows[0]["AudioName"].ToString()!="")
				{
					model.AudioName=ds.Tables[0].Rows[0]["AudioName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Audio"]!=null && ds.Tables[0].Rows[0]["Audio"].ToString()!="")
				{
					model.Audio=(byte[])ds.Tables[0].Rows[0]["Audio"];
				}
				if(ds.Tables[0].Rows[0]["Time"]!=null && ds.Tables[0].Rows[0]["Time"].ToString()!="")
				{
					model.Time=DateTime.Parse(ds.Tables[0].Rows[0]["Time"].ToString());
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
			strSql.Append("select Emergency_id,Event_id,UserName,EventStatus,Note,Pic,VedioName,Vedio,AudioName,Audio,Time ");
			strSql.Append(" FROM Emergency ");
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
			strSql.Append(" Emergency_id,Event_id,UserName,EventStatus,Note,Pic,VedioName,Vedio,AudioName,Audio,Time ");
			strSql.Append(" FROM Emergency ");
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
			strSql.Append("select count(1) FROM Emergency ");
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
				strSql.Append("order by T.Emergency_id desc");
			}
			strSql.Append(")AS Row, T.*  from Emergency T ");
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
			parameters[0].Value = "Emergency";
			parameters[1].Value = "Emergency_id";
			parameters[2].Value = PageSize;
			parameters[3].Value = PageIndex;
			parameters[4].Value = 0;
			parameters[5].Value = 0;
			parameters[6].Value = strWhere;	
			return DbHelperSQL.RunProcedure("UP_GetRecordByPage",parameters,"ds");
		}*/

		#endregion  Method
	}
}

