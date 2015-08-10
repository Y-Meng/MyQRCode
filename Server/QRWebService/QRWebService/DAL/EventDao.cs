using System;
using System.Data;
using System.Text;
using System.Data.SqlClient;
using Maticsoft.DBUtility;//Please add references
namespace QR.DAL
{
	/// <summary>
	/// 数据访问类:Event
	/// </summary>
	public partial class Event
	{
		public Event()
		{}
		#region  Method

		/// <summary>
		/// 得到最大ID
		/// </summary>
		public int GetMaxId()
		{
		return DbHelperSQL.GetMaxID("Event_id", "Event"); 
		}

		/// <summary>
		/// 是否存在该记录
		/// </summary>
		public bool Exists(int Event_id)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select count(1) from Event");
			strSql.Append(" where Event_id=@Event_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Event_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Event_id;

			return DbHelperSQL.Exists(strSql.ToString(),parameters);
		}


		/// <summary>
		/// 增加一条数据
		/// </summary>
		public int Add(QR.Model.Event model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("insert into Event(");
			strSql.Append("UserName,QRcode,QRdetail,QRstatus,Time,Pic,PicName,VideoName,Video,AudioName,Audio)");
			strSql.Append(" values (");
			strSql.Append("@UserName,@QRcode,@QRdetail,@QRstatus,@Time,@Pic,@PicName,@VideoName,@Video,@AudioName,@Audio)");
			strSql.Append(";select @@IDENTITY");
			SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@QRcode", SqlDbType.NVarChar,50),
					new SqlParameter("@QRdetail", SqlDbType.NVarChar,50),
					new SqlParameter("@QRstatus", SqlDbType.NVarChar,50),
					new SqlParameter("@Time", SqlDbType.DateTime),
					new SqlParameter("@Pic", SqlDbType.Image),
					new SqlParameter("@PicName", SqlDbType.NVarChar,50),
					new SqlParameter("@VideoName", SqlDbType.NVarChar,50),
					new SqlParameter("@Video", SqlDbType.VarBinary),
					new SqlParameter("@AudioName", SqlDbType.NVarChar,50),
					new SqlParameter("@Audio", SqlDbType.VarBinary)};
			parameters[0].Value = model.UserName;
			parameters[1].Value = model.QRcode;
			parameters[2].Value = model.QRdetail;
			parameters[3].Value = model.QRstatus;
			parameters[4].Value = model.Time;
			parameters[5].Value = model.Pic;
			parameters[6].Value = model.PicName;
			parameters[7].Value = model.VideoName;
			parameters[8].Value = model.Video;
			parameters[9].Value = model.AudioName;
			parameters[10].Value = model.Audio;

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
		public bool Update(QR.Model.Event model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("update Event set ");
			strSql.Append("UserName=@UserName,");
			strSql.Append("QRcode=@QRcode,");
			strSql.Append("QRdetail=@QRdetail,");
			strSql.Append("QRstatus=@QRstatus,");
			strSql.Append("Time=@Time,");
			strSql.Append("Pic=@Pic,");
			strSql.Append("PicName=@PicName,");
			strSql.Append("VideoName=@VideoName,");
			strSql.Append("Video=@Video,");
			strSql.Append("AudioName=@AudioName,");
			strSql.Append("Audio=@Audio");
			strSql.Append(" where Event_id=@Event_id");
			SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@QRcode", SqlDbType.NVarChar,50),
					new SqlParameter("@QRdetail", SqlDbType.NVarChar,50),
					new SqlParameter("@QRstatus", SqlDbType.NVarChar,50),
					new SqlParameter("@Time", SqlDbType.DateTime),
					new SqlParameter("@Pic", SqlDbType.Image),
					new SqlParameter("@PicName", SqlDbType.NVarChar,50),
					new SqlParameter("@VideoName", SqlDbType.NVarChar,50),
					new SqlParameter("@Video", SqlDbType.VarBinary),
					new SqlParameter("@AudioName", SqlDbType.NVarChar,50),
					new SqlParameter("@Audio", SqlDbType.VarBinary),
					new SqlParameter("@Event_id", SqlDbType.Int,4)};
			parameters[0].Value = model.UserName;
			parameters[1].Value = model.QRcode;
			parameters[2].Value = model.QRdetail;
			parameters[3].Value = model.QRstatus;
			parameters[4].Value = model.Time;
			parameters[5].Value = model.Pic;
			parameters[6].Value = model.PicName;
			parameters[7].Value = model.VideoName;
			parameters[8].Value = model.Video;
			parameters[9].Value = model.AudioName;
			parameters[10].Value = model.Audio;
			parameters[11].Value = model.Event_id;

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
		public bool Delete(int Event_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Event ");
			strSql.Append(" where Event_id=@Event_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Event_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Event_id;

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
		public bool DeleteList(string Event_idlist )
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Event ");
			strSql.Append(" where Event_id in ("+Event_idlist + ")  ");
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
		public QR.Model.Event GetModel(int Event_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select  top 1 Event_id,UserName,QRcode,QRdetail,QRstatus,Time,Pic,PicName,VideoName,Video,AudioName,Audio from Event ");
			strSql.Append(" where Event_id=@Event_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Event_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Event_id;

			QR.Model.Event model=new QR.Model.Event();
			DataSet ds=DbHelperSQL.Query(strSql.ToString(),parameters);
			if(ds.Tables[0].Rows.Count>0)
			{
				if(ds.Tables[0].Rows[0]["Event_id"]!=null && ds.Tables[0].Rows[0]["Event_id"].ToString()!="")
				{
					model.Event_id=int.Parse(ds.Tables[0].Rows[0]["Event_id"].ToString());
				}
				if(ds.Tables[0].Rows[0]["UserName"]!=null && ds.Tables[0].Rows[0]["UserName"].ToString()!="")
				{
					model.UserName=ds.Tables[0].Rows[0]["UserName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["QRcode"]!=null && ds.Tables[0].Rows[0]["QRcode"].ToString()!="")
				{
					model.QRcode=ds.Tables[0].Rows[0]["QRcode"].ToString();
				}
				if(ds.Tables[0].Rows[0]["QRdetail"]!=null && ds.Tables[0].Rows[0]["QRdetail"].ToString()!="")
				{
					model.QRdetail=ds.Tables[0].Rows[0]["QRdetail"].ToString();
				}
				if(ds.Tables[0].Rows[0]["QRstatus"]!=null && ds.Tables[0].Rows[0]["QRstatus"].ToString()!="")
				{
					model.QRstatus=ds.Tables[0].Rows[0]["QRstatus"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Time"]!=null && ds.Tables[0].Rows[0]["Time"].ToString()!="")
				{
					model.Time=DateTime.Parse(ds.Tables[0].Rows[0]["Time"].ToString());
				}
				if(ds.Tables[0].Rows[0]["Pic"]!=null && ds.Tables[0].Rows[0]["Pic"].ToString()!="")
				{
					model.Pic=(byte[])ds.Tables[0].Rows[0]["Pic"];
				}
				if(ds.Tables[0].Rows[0]["PicName"]!=null && ds.Tables[0].Rows[0]["PicName"].ToString()!="")
				{
					model.PicName=(string)ds.Tables[0].Rows[0]["PicName"];
				}
				if(ds.Tables[0].Rows[0]["VideoName"]!=null && ds.Tables[0].Rows[0]["VideoName"].ToString()!="")
				{
					model.VideoName=ds.Tables[0].Rows[0]["VideoName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Video"]!=null && ds.Tables[0].Rows[0]["Video"].ToString()!="")
				{
					model.Video=(byte[])ds.Tables[0].Rows[0]["Video"];
				}
				if(ds.Tables[0].Rows[0]["AudioName"]!=null && ds.Tables[0].Rows[0]["AudioName"].ToString()!="")
				{
					model.AudioName=ds.Tables[0].Rows[0]["AudioName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Audio"]!=null && ds.Tables[0].Rows[0]["Audio"].ToString()!="")
				{
					model.Audio=(byte[])ds.Tables[0].Rows[0]["Audio"];
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
			strSql.Append("select Event_id,UserName,QRcode,QRdetail,QRstatus,Time,Pic,PicName,VideoName,Video,AudioName,Audio ");
			strSql.Append(" FROM Event ");
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
			strSql.Append(" Event_id,UserName,QRcode,QRdetail,QRstatus,Time,Pic,PicName,VideoName,Video,AudioName,Audio ");
			strSql.Append(" FROM Event ");
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
			strSql.Append("select count(1) FROM Event ");
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
				strSql.Append("order by T.Event_id desc");
			}
			strSql.Append(")AS Row, T.*  from Event T ");
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
			parameters[0].Value = "Event";
			parameters[1].Value = "Event_id";
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

