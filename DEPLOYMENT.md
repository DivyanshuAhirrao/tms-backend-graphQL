# Backend Deployment Guide - Render.com

## 📦 What's Included for Deployment

✅ **render.yaml** - Automated Render configuration
✅ **system.properties** - Java 21 runtime specification
✅ **Updated application.properties** - Environment variable support
✅ **Enhanced CORS config** - Supports multiple origins and patterns
✅ **.env.example** - Sample environment variables
✅ **.gitignore** - Excludes sensitive files

## 🚀 Deployment Steps

### 1. Push to GitHub

```bash
cd backend
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/tms-backend.git
git push -u origin main
```

### 2. Deploy on Render

1. Go to https://render.com/
2. New → Web Service
3. Connect GitHub repository: `tms-backend`
4. Configure:
   - **Name**: tms-backend
   - **Region**: Choose closest
   - **Branch**: main
   - **Runtime**: Java
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `java -jar target/tms-backend-1.0.0.jar`
   - **Instance Type**: Free

### 3. Set Environment Variables

In Render dashboard, add these:

```env
PORT=8080
JWT_SECRET=YourSecureRandomSecret123!ChangeThis
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=https://your-frontend.vercel.app,http://localhost:3000
H2_CONSOLE_ENABLED=true
```

### 4. Deploy & Test

- Click "Create Web Service"
- Wait 5-10 minutes for deployment
- Test at: `https://your-backend.onrender.com/graphiql`

## 🔧 Configuration Details

### Environment Variables Explained

| Variable | Purpose | Example Value |
|----------|---------|---------------|
| `PORT` | Server port | `8080` |
| `JWT_SECRET` | Secret key for JWT tokens | `Random123SecureKey!` |
| `JWT_EXPIRATION` | Token validity (ms) | `86400000` (24 hours) |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend URLs | `https://app.vercel.app,http://localhost:3000` |
| `H2_CONSOLE_ENABLED` | Enable H2 database console | `true` or `false` |

### CORS Configuration

The backend supports:
- ✅ Multiple comma-separated origins
- ✅ Wildcard for Vercel preview deployments (`*.vercel.app`)
- ✅ Local development URLs
- ✅ Credentials support for cookies/auth

### Build Process

1. Maven downloads dependencies
2. Compiles Java 21 source code
3. Runs tests (skipped with `-DskipTests`)
4. Packages as JAR file: `target/tms-backend-1.0.0.jar`

### Runtime

- Java 21 (specified in `system.properties`)
- H2 in-memory database (resets on restart)
- Auto-loads 15 sample shipments from `data.sql`

## 🧪 Testing Deployed Backend

### 1. Access GraphiQL

Open: `https://your-backend.onrender.com/graphiql`

### 2. Test Login Mutation

```graphql
mutation {
  login(username: "admin", password: "admin123") {
    token
    user {
      id
      username
      email
      role
    }
  }
}
```

### 3. Test Query (with token)

Copy token from login response, add to HTTP Headers:
```json
{
  "Authorization": "Bearer YOUR_TOKEN_HERE"
}
```

Then run:
```graphql
query {
  shipments(page: {page: 0, size: 5}) {
    content {
      id
      shipmentNumber
      shipperName
      status
    }
    totalElements
  }
}
```

## 🐛 Troubleshooting

### Build Fails

**Check logs for**:
- Java version mismatch → Verify `system.properties`
- Maven errors → Check `pom.xml` syntax
- Memory issues → Free tier has limits

**Solution**: Review Render logs, fix issues, push update

### App Crashes After Deployment

**Common causes**:
1. Wrong JAR filename in start command
2. Missing environment variables
3. Port binding issues

**Check**:
- Start command: `java -jar target/tms-backend-1.0.0.jar`
- `PORT` environment variable is set
- Logs show "Started TmsApplication"

### CORS Errors

**Symptoms**: Frontend gets blocked requests

**Solutions**:
1. Verify `CORS_ALLOWED_ORIGINS` includes your frontend URL
2. No spaces in the environment variable value
3. Include both `https://` and any `www.` variants
4. Redeploy after changing CORS settings

### Slow First Request

**This is normal** for Render free tier:
- Service spins down after 15 minutes of inactivity
- First request takes 30-60 seconds to wake up
- Subsequent requests are fast

**Workaround**: Use a cron service to ping every 10 minutes

## 📊 Monitoring

### View Logs

Render Dashboard → Your Service → Logs tab

Look for:
- ✅ "Started TmsApplication in X seconds"
- ✅ "Tomcat started on port(s): 8080"
- ❌ Error stack traces
- ❌ Out of memory errors

### Metrics

Free tier includes:
- CPU usage
- Memory usage
- Request count
- Response times

## 🔄 Updates & Redeployment

### Auto-Deploy on Git Push

```bash
# Make changes
git add .
git commit -m "Update description"
git push origin main
# Render auto-deploys (takes 3-5 minutes)
```

### Manual Deploy

Render Dashboard → Manual Deploy → Deploy Latest Commit

### Rollback

Render Dashboard → Deployments → Pick previous deployment → Redeploy

## 🔒 Security Best Practices

1. **JWT_SECRET**: Use strong, random value (32+ characters)
2. **Environment Variables**: Never commit to GitHub
3. **CORS**: Only allow your specific frontend domains
4. **H2 Console**: Disable in production (`H2_CONSOLE_ENABLED=false`)
5. **HTTPS**: Render provides free SSL (always use `https://`)

## 📈 Performance Tips

1. **Database**: Consider PostgreSQL for production (not H2)
2. **Caching**: Already enabled with Spring Cache
3. **Connection Pool**: Configured in `application.properties`
4. **Query Optimization**: Indexes already added to entities

## 🆙 Upgrading Beyond Free Tier

For production use, consider:
- **Starter Plan** ($7/month): No spin-down, better performance
- **PostgreSQL**: Persistent database ($7/month)
- **Custom domain**: Free SSL certificate included

## ✅ Deployment Checklist

- [ ] Code pushed to GitHub
- [ ] Render service created
- [ ] All environment variables set correctly
- [ ] Deployment successful (check logs)
- [ ] GraphiQL accessible and working
- [ ] Login mutation returns token
- [ ] Shipments query returns data
- [ ] CORS configured for frontend
- [ ] No errors in logs

## 🎯 Success Criteria

Your backend is deployed correctly if:
1. ✅ GraphiQL URL opens without errors
2. ✅ Login mutation returns valid token
3. ✅ Protected queries work with auth token
4. ✅ No CORS errors when accessing from frontend
5. ✅ All 15 sample shipments load successfully

---

**Deployment Time**: ~5-10 minutes
**Your Backend URL**: `https://tms-backend-xxxx.onrender.com`
