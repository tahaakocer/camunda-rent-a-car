const express = require('express');
const path = require('path');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Serve static files....
app.use(express.static(__dirname + '/'));

app.use('/rest/log', createProxyMiddleware({ target: 'http://test-next-vfnet.vndev.vodafone.local', changeOrigin: true }));
app.use('/rest/orderrequest/orderitem', createProxyMiddleware({ target: 'http://test-next-vfnet.vndev.vodafone.local', changeOrigin: true }));
app.use('/rest/', createProxyMiddleware({ target: 'http://camunda-test-next-vfnet.vndev.vodafone.local', changeOrigin: true }));
app.use('/token/', createProxyMiddleware({ target: 'http://camunda-test-next-vfnet.vndev.vodafone.local', changeOrigin: true }));
app.use('/orderrequests/', createProxyMiddleware({ target: 'http://test-next-vfnet.vndev.vodafone.local', changeOrigin: true }));


// Send all requests to index.html
app.get('/*', function(req, res) {
    res.sendFile(path.join(__dirname + '/viewer.html'));
});


// default Heroku PORT
app.listen(process.env.PORT || 3000);
