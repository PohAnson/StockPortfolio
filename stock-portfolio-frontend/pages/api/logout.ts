import { withIronSessionApiRoute } from "iron-session/next";
import { sessionOptions } from "../../lib/session";

export default withIronSessionApiRoute(function logoutRoute(req, res) {
  req.session.destroy();
  res.redirect("/");
}, sessionOptions);
