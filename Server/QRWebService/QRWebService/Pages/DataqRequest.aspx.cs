using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace QRWebService.Pages
{
    public partial class DataqRequest : System.Web.UI.Page
    {
        int requestcode;
        int OID;

        protected void Page_Load(object sender, EventArgs e)
        {
            //requestcode = Convert.ToInt32(Request.Params.Get(0));
            //OID = Convert.ToInt32(Request.Params.Get(1));
           // OID = Convert.ToInt32(Request.QueryString["OID"]);

           // GISOperation operation = new GISOperation();
            //string img = operation.RequestData(requestcode,OID);

            //Response.Write("<div><p>"+img+"</p></div>");

            //byte[] byteImg = Convert.FromBase64String(img);
            //Response.BinaryWrite(byteImg);
        }
    }
}