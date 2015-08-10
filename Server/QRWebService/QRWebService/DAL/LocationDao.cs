using System;
using System.Data;
using System.Text;
using System.Data.SqlClient;
using Maticsoft.DBUtility;//Please add references
namespace QR.DAL
{
	/// <summary>
	/// 数据访问类:Location
	/// </summary>
	public partial class Location
	{
		public Location()
		{}
		#region  Method

		/// <summary>
		/// 得到最大ID
		/// </summary>
		public int GetMaxId()
		{
		return DbHelperSQL.GetMaxID("Location_id", "Location"); 
		}

		/// <summary>
		/// 是否存在该记录
		/// </summary>
		public bool Exists(int Location_id)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select count(1) from Location");
			strSql.Append(" where Location_id=@Location_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Location_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Location_id;

			return DbHelperSQL.Exists(strSql.ToString(),parameters);
		}


		/// <summary>
		/// 增加一条数据
		/// </summary>
		public int Add(QR.Model.Location model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("insert into Location(");
			strSql.Append("UserName,Latitude,Longitude,Time)");
			strSql.Append(" values (");
			strSql.Append("@UserName,@Latitude,@Longitude,@Time)");
			strSql.Append(";select @@IDENTITY");
			SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@Latitude", SqlDbType.Float,8),
					new SqlParameter("@Longitude", SqlDbType.Float,8),
					new SqlParameter("@Time", SqlDbType.DateTime)};
			parameters[0].Value = model.UserName;
			parameters[1].Value = model.Latitude;
			parameters[2].Value = model.Longitude;
			parameters[3].Value = model.Time;

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
		public bool Update(QR.Model.Location model)
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("update Location set ");
			strSql.Append("UserName=@UserName,");
			strSql.Append("Latitude=@Latitude,");
			strSql.Append("Longitude=@Longitude,");
			strSql.Append("Time=@Time");
			strSql.Append(" where Location_id=@Location_id");
			SqlParameter[] parameters = {
					new SqlParameter("@UserName", SqlDbType.NVarChar,50),
					new SqlParameter("@Latitude", SqlDbType.Float,8),
					new SqlParameter("@Longitude", SqlDbType.Float,8),
					new SqlParameter("@Time", SqlDbType.DateTime),
					new SqlParameter("@Location_id", SqlDbType.Int,4)};
			parameters[0].Value = model.UserName;
			parameters[1].Value = model.Latitude;
			parameters[2].Value = model.Longitude;
			parameters[3].Value = model.Time;
			parameters[4].Value = model.Location_id;

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
		public bool Delete(int Location_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Location ");
			strSql.Append(" where Location_id=@Location_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Location_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Location_id;

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
		public bool DeleteList(string Location_idlist )
		{
			StringBuilder strSql=new StringBuilder();
			strSql.Append("delete from Location ");
			strSql.Append(" where Location_id in ("+Location_idlist + ")  ");
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
		public QR.Model.Location GetModel(int Location_id)
		{
			
			StringBuilder strSql=new StringBuilder();
			strSql.Append("select  top 1 Location_id,UserName,Latitude,Longitude,Time from Location ");
			strSql.Append(" where Location_id=@Location_id");
			SqlParameter[] parameters = {
					new SqlParameter("@Location_id", SqlDbType.Int,4)
			};
			parameters[0].Value = Location_id;

			QR.Model.Location model=new QR.Model.Location();
			DataSet ds=DbHelperSQL.Query(strSql.ToString(),parameters);
			if(ds.Tables[0].Rows.Count>0)
			{
				if(ds.Tables[0].Rows[0]["Location_id"]!=null && ds.Tables[0].Rows[0]["Location_id"].ToString()!="")
				{
					model.Location_id=int.Parse(ds.Tables[0].Rows[0]["Location_id"].ToString());
				}
				if(ds.Tables[0].Rows[0]["UserName"]!=null && ds.Tables[0].Rows[0]["UserName"].ToString()!="")
				{
					model.UserName=ds.Tables[0].Rows[0]["UserName"].ToString();
				}
				if(ds.Tables[0].Rows[0]["Latitude"]!=null && ds.Tables[0].Rows[0]["Latitude"].ToString()!="")
				{
					model.Latitude=decimal.Parse(ds.Tables[0].Rows[0]["Latitude"].ToString());
				}
				if(ds.Tables[0].Rows[0]["Longitude"]!=null && ds.Tables[0].Rows[0]["Longitude"].ToString()!="")
				{
					model.Longitude=decimal.Parse(ds.Tables[0].Rows[0]["Longitude"].ToString());
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
			strSql.Append("select Location_id,UserName,Latitude,Longitude,Time ");
			strSql.Append(" FROM Location ");
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
			strSql.Append(" Location_id,UserName,Latitude,Longitude,Time ");
			strSql.Append(" FROM Location ");
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
			strSql.Append("select count(1) FROM Location ");
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
				strSql.Append("order by T.Location_id desc");
			}
			strSql.Append(")AS Row, T.*  from Location T ");
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
			parameters[0].Value = "Location";
			parameters[1].Value = "Location_id";
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

